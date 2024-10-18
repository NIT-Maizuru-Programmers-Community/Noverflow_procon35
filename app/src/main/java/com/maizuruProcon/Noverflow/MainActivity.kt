package com.maizuruProcon.Noverflow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageView
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.content.Context
import com.maizuruProcon.Noverflow.botton.SecondActivity
import android.graphics.Color
import androidx.activity.enableEdgeToEdge
import com.maizuruProcon.Noverflow.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ListenerRegistration
import android.graphics.BitmapFactory
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import getMapFieldValueSum
import updateFieldDataWithOption


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var qrImage: ImageView
    private lateinit var btnstart: Button
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 5*60*1000 // 5分
    private lateinit var timerFragment: TimerFragment
    private lateinit var listenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        initializeCounts()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setContentView(binding.root)

        // アプリ起動時にボタンの初期状態を設定
        binding.btnStart.apply {
            setBackgroundResource(R.drawable.design) // 背景を設定
            text = "捨てる" // テキストを設定
            textSize = 60f // テキストサイズを設定
            isEnabled = true // ボタンを有効にする
        }

        // SharedPreferencesからボタンの状態を読み取る
        val sharedPref = getSharedPreferences("ButtonState", Context.MODE_PRIVATE)
        val isBtnStartDisabled = sharedPref.getBoolean("btnStartDisabled", false)
        val btnStartText = sharedPref.getString("btnStartText", "捨てる")

        // ボタンの状態を設定
        binding.btnStart.apply {
            text = btnStartText // SharedPreferencesから読み取ったテキストを設定
            if (isBtnStartDisabled) {
                setBackgroundColor(Color.GRAY) // 背景を灰色にする
                isEnabled = false // ボタンを無効にする
            } else {
                setBackgroundResource(R.drawable.design) // 元の背景に戻す
                isEnabled = true // ボタンを有効にする
            }
        }

        // ボタンを取得
        val imageButton: ImageButton = findViewById(R.id.button)
        val mapButton :Button =findViewById(R.id.mapButton)
        val clearButton: Button = findViewById(R.id.clearButton)


        binding.clearButton.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            timerFragment.stopTimer()
            qrImage.setImageBitmap(null)
            qrImage.setBackgroundResource(R.drawable.qr_code_border)

            val sharedPref = getSharedPreferences("ButtonState", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("btnStartDisabled", false)
                putString("btnStartText", "捨てる")
                apply()
            }

            binding.btnStart.apply {
                setBackgroundResource(R.drawable.design)
                textSize = 80f
                text = "捨てる"
                isEnabled = true
            }

            val countsPref = getSharedPreferences("CountsData", Context.MODE_PRIVATE)
            with(countsPref.edit()) {
                putInt("burningGarbage", 0)
                putInt("plasticGarbage", 0)
                putInt("bottles", 0)
                putInt("cans", 0)
                apply()
            }
            val retrievedBurningGarbage = countsPref.getInt("burningGarbage", -1)
            val retrievedPlasticGarbage = countsPref.getInt("plasticGarbage", -1)
            val retrievedBottles = countsPref.getInt("bottles", -1)
            val retrievedCans = countsPref.getInt("cans", -1)
            Log.d("CountsDebug", "Retrieved - Burning Garbage: $retrievedBurningGarbage, Plastic Garbage: $retrievedPlasticGarbage, Bottles: $retrievedBottles, Cans: $retrievedCans")
        }

        qrImage = findViewById(R.id.qr_code_image)
        btnstart = findViewById(R.id.btnStart)

        // ボタンが押された時の処理(画面遷移)
        btnstart.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)// Intentを作成してsecondActivityに遷移
            startActivity(intent)
        }
        imageButton.setOnClickListener {
            val intent = Intent(this, account::class.java)// Intentを作成してaccountActivityに遷移
            startActivity(intent)
        }
        mapButton.setOnClickListener{
            val intent= Intent(this, FourActivity::class.java)// Intentを作成してfourActivityに遷移
            startActivity(intent)
        }

        var total: Int?

        getMapFieldValueSum(// ごみを捨てた回数のカウント
            collectionName = "noverflow-apps",  // コレクション名
            documentId = "pixel4a",    // ドキュメントID
            fieldName = "garbages"      // 取得したいフィールド名
        ) { result ->
            total = result
            total?.let {
                Log.d("Firestore", "取得した合計値: $total")

                val remainder = it % 3 // 3で割った余りを計算（レベルバーの値）
                updateImage(remainder, imageButton)// 余りの値に基づいて画像を変更
            } ?: run {
                Log.e("Firestore", "合計値の取得に失敗しました")
            }
        }

        listenerRegistration = FirebaseFirestore.getInstance()
            .collection("garbageBoxes") // コレクション名を指定
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val flag = document.getBoolean("flag") ?: false
                        if (flag) {
                            val countsPref = getSharedPreferences("CountsData", Context.MODE_PRIVATE)
                            val retrievedBurningGarbage = countsPref.getInt("burningGarbage", -1)
                            val retrievedPlasticGarbage = countsPref.getInt("plasticGarbage", -1)
                            val retrievedBottles = countsPref.getInt("bottles", -1)
                            val retrievedCans = countsPref.getInt("cans", -1)

                            Log.d("CountsDebug", "Retrieved - Burning Garbage: $retrievedBurningGarbage, Plastic Garbage: $retrievedPlasticGarbage, Bottles: $retrievedBottles, Cans: $retrievedCans")

                            // Firestoreにアップロード
                            updateCountsInFirestore(retrievedBurningGarbage, retrievedPlasticGarbage, retrievedBottles, retrievedCans)

                            handler.removeCallbacksAndMessages(null)
                            timerFragment.stopTimer()
                            qrImage.setImageBitmap(null)
                            qrImage.setBackgroundResource(R.drawable.qr_code_border)

                            with(sharedPref.edit()) {
                                putBoolean("btnStartDisabled", false)
                                putString("btnStartText", "捨てる")
                                apply()
                            }

                            binding.btnStart.apply {
                                setBackgroundResource(R.drawable.design)
                                textSize = 80f
                                text = "捨てる"
                                isEnabled = true
                            }

                            with(countsPref.edit()) {
                                putInt("burningGarbage", 0)
                                putInt("plasticGarbage", 0)
                                putInt("bottles", 0)
                                putInt("cans", 0)
                                apply()
                            }
                            Log.d("CountsDebug", "Retrieved - Burning Garbage: $retrievedBurningGarbage, Plastic Garbage: $retrievedPlasticGarbage, Bottles: $retrievedBottles, Cans: $retrievedCans")

                            updateAllFlagsToFalse()
                        }
                    }
                }
            }

        val byteArray = intent.getByteArrayExtra("QR_CODE")// IntentからQRコードのバイト配列を取得

        byteArray?.let {// バイト配列が存在する場合、BitmapにデコードしてImageViewに設定
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            qrImage.setImageBitmap(bitmap) // ImageViewにQRコードを表示
        }

        if (savedInstanceState == null) {// Fragmentを追加
            timerFragment = TimerFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, timerFragment)
                .commit()
        } else {
            timerFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as TimerFragment
        }

        if (btnStartText == "利用不可") {//利用不可の時だけタイマーを動かす
            startQrUpdateTimer()
            timerFragment.setTimerCallback(object : TimerCallback {
                override fun onTimerFinished() {
                    stopQrUpdateTimer()
                    qrImage.setImageBitmap(null)
                    qrImage.setBackgroundResource(R.drawable.qr_code_border) // デフォルトの背景画像に戻す

                    with(sharedPref.edit()) {// SharedPreferencesの状態をリセット
                        putBoolean("btnStartDisabled", false)
                        putString("btnStartText", "捨てる") // ボタンのテキストを「捨てる」に戻す
                        apply()
                    }

                    binding.btnStart.apply {// ボタンの状態を元に戻す
                        setBackgroundResource(R.drawable.design) // 元の背景に戻す
                        textSize = 60f // テキストサイズを設定
                        text = "捨てる" // テキストを「捨てる」に戻す
                        isEnabled = true // ボタンを有効にする
                    }
                }
            })
            timerFragment.startTimer()
        }
    }

    private fun updateImage(remainder: Int?, imageButton: ImageButton) {// 画像を更新する関数
        when (remainder) {
            0, 3 -> imageButton.setImageResource(R.drawable.count1)  // 数字が0,3の時の画像
            1 -> imageButton.setImageResource(R.drawable.count2)  // 数字が1の時の画像
            2 -> imageButton.setImageResource(R.drawable.count3)  // 数字が2の時の画像
        }
    }

    override fun onStop() {
        super.onStop()
        val countsPref = getSharedPreferences("CountsData", Context.MODE_PRIVATE)
        with(countsPref.edit()) {
            putInt("burningGarbage", 0)
            putInt("plasticGarbage", 0)
            putInt("bottles", 0)
            putInt("cans", 0)
            apply()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtils.stopListeningToFlagChanges() // リスナーを停止
    }

    private fun stopQrUpdateTimer() {// タイマーを停止するメソッド
        handler.removeCallbacksAndMessages(null)
    }

    private fun startQrUpdateTimer() {// タイマーを開始するメソッド
        handler.post(object : Runnable {
            override fun run() {
                updateQrCode()
                handler.postDelayed(this, updateInterval)
            }
        })
    }

    private fun updateQrCode() {// QRコードを更新するメソッド
        val randomNumber = QRCodeUtils.generateRandomFourDigitNumber().toString()

        updateFieldDataWithOption(
            collectionName = "noverflow-apps",
            documentId = "pixel4a",
            fieldName = "token",
            value = randomNumber,
            updateMode = UpdateMode.SET,
            onSuccess = {
                Log.d("Firestore", "Field updated successfully")
            },
            onFailure = { exception ->
                Log.e("Firestore", "Error updating field", exception)
            }
        )

        val qrBitmap = QRCodeUtils.createQrCode(randomNumber)
        qrBitmap?.let {
            qrImage.setImageBitmap(it)
        }
    }

    private fun initializeCounts() {
        val countsPref = getSharedPreferences("CountsData", Context.MODE_PRIVATE)

        // 初期値を設定する条件を指定（ここでは例として、デフォルト値が設定されているかどうかをチェック）
        if (countsPref.getInt("burningGarbage", -1) == -1) {
            // 初期値を保存
            with(countsPref.edit()) {
                putInt("burningGarbage", 0)
                putInt("plasticGarbage", 0)
                putInt("bottles", 0)
                putInt("cans", 0)
                apply()
            }
            Log.d("CountsData", "Initialized CountsData with zeros.")
        }
    }
    private fun updateCountsInFirestore(burningGarbage: Int, plasticGarbage: Int, bottles: Int, cans: Int) {
        val db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
            "burningGarbage" to burningGarbage,
            "plasticGarbage" to plasticGarbage,
            "bottles" to bottles,
            "cans" to cans
        )

        for ((key, value) in data) {
            updateFieldDataWithOption(
                collectionName = "noverflow-apps",
                documentId = "pixel4a",
                fieldName = "garbages.$key",  // フィールド名を "garbages.<key>" に
                value = value,                // 値を更新
                updateMode = UpdateMode.INCREMENT,
                onSuccess = {
                    Log.d("Firestore", "$key updated successfully with value $value")
                },
                onFailure = { exception ->
                    Log.e("Firestore", "Error updating $key", exception)
                }
            )
        }
    }
    private fun updateAllFlagsToFalse() {
        val db = FirebaseFirestore.getInstance()
        db.collection("garbageBoxes") // 対象のコレクション名を指定
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val docId = document.id
                    db.collection("garbageBoxes").document(docId)
                        .update("flag", false)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Document $docId successfully updated with flag=false")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error updating document $docId", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting documents", e)
            }
    }
}
