package com.maizuruProcon.Noverflow

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
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
import com.maizuruProcon.Noverflow.databinding.ActivityMainBinding
import android.graphics.BitmapFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var qrImage: ImageView
    private lateinit var btnstart: Button
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 5*  1000 // 5分
    private lateinit var timerFragment: TimerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets///apple apple
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // アプリ起動時にボタンの初期状態を設定
        binding.btnStart.apply {
            setBackgroundResource(R.drawable.design) // 背景を設定
            text = "捨てる" // テキストを設定
            textSize = 80f // テキストサイズを設定
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
        val button: Button = findViewById(R.id.test)
        val resetButton: Button = findViewById(R.id.resetButton)
        val mapButton :Button =findViewById(R.id.mapButton)

        qrImage = findViewById(R.id.qr_code_image)
        btnstart = findViewById(R.id.btnStart)

        // ボタンが押された時の処理(画面遷移)
        btnstart.setOnClickListener {
            // Intentを作成してsecondActivityに遷移
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        imageButton.setOnClickListener {
            // Intentを作成してaccountActivityに遷移
            val intent = Intent(this, account::class.java)
            startActivity(intent)
        }
        mapButton.setOnClickListener{
            // Intentを作成してfourActivityに遷移
            val intent= Intent(this, FourActivity::class.java)
            startActivity(intent)
        }

        // ごみを捨てた回数のカウント

        // SharedPreferencesの読み込み
        val sharedPref2 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        Singleton.total = sharedPref2.getInt("total", 0)
        Singleton.moeru = sharedPref2.getInt("moeru", 0)
        Singleton.pet = sharedPref2.getInt("pet", 0)
        Singleton.plastic = sharedPref2.getInt("plastic", 0)
        Singleton.kan = sharedPref2.getInt("kan", 0)

        //仮のボタンを押したら+1
        button.setOnClickListener {
            Singleton.total += 1

            //レベルバー,レベルの計算
            // SharedPreferencesにデータを保存
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

            with(sharedPref.edit()) {
                putInt("total", Singleton.total)
                apply()
            }

            // 3で割った余りを計算（レベルバーの値）
            Singleton.remainder = Singleton.total % 3

            // 3で割ったときの商を計算（レベルの値）
            Singleton.quotient = Singleton.total / 3

            // 余りの値に基づいて画像を変更
            if (Singleton.remainder == 3 || Singleton.remainder == 0) {
                imageButton.setImageResource(R.drawable.count1)  // 数字が0,3の時の画像
            } else if (Singleton.remainder == 1) {
                imageButton.setImageResource(R.drawable.count2)  // 数字が1の時の画像
            } else if (Singleton.remainder == 2) {
                imageButton.setImageResource(R.drawable.count3)  // 数字が2の時の画像
            }
        }

        //リセットボタンの実装
        resetButton.setOnClickListener {
            Singleton.total = 0

            // SharedPreferencesにデータを保存
            with(sharedPref2.edit()) {
                putInt("total", Singleton.total)
                apply()
            }

            // 3で割った余りを計算
            Singleton.remainder = Singleton.total % 3

            // 余りを求めたときの商を計算
            Singleton.quotient = Singleton.total / 3

            // 数字の値に基づいて画像を変更
            if (Singleton.remainder == 3 || Singleton.remainder == 0) {
                imageButton.setImageResource(R.drawable.count1)  // 数字が0,3の時の画像
            } else if (Singleton.remainder == 1) {
                imageButton.setImageResource(R.drawable.count2)  // 数字が1の時の画像
            } else if (Singleton.remainder == 2) {
                imageButton.setImageResource(R.drawable.count3)  // 数字が2の時の画像
            }
        }

        // 画像を更新する関数
        fun updateImage(total: Int, imageButton: ImageButton) {
            val remainder = total % 3
            when (remainder) {
                0, 3 -> imageButton.setImageResource(R.drawable.count1)  // 数字が0,3の時の画像
                1 -> imageButton.setImageResource(R.drawable.count2)  // 数字が1の時の画像
                2 -> imageButton.setImageResource(R.drawable.count3)  // 数字が2の時の画像
            }
        }

        // アプリ起動時に画像を設定
        updateImage(Singleton.total, imageButton)

        // IntentからQRコードのバイト配列を取得
        val byteArray = intent.getByteArrayExtra("QR_CODE")

        // バイト配列が存在する場合、BitmapにデコードしてImageViewに設定
        byteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            qrImage.setImageBitmap(bitmap) // ImageViewにQRコードを表示
        }

        // Fragmentを追加
        if (savedInstanceState == null) {
            timerFragment = TimerFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, timerFragment)
                .commit()
        } else {
            timerFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as TimerFragment
        }

        if (btnStartText == "利用不可") {
            startQrUpdateTimer()
            timerFragment.setTimerCallback(object : TimerCallback {
                override fun onTimerFinished() {
                    stopQrUpdateTimer()
                    qrImage.setImageBitmap(null)
                    qrImage.setBackgroundResource(R.drawable.qr_code_border) // デフォルトの背景画像に戻す

                    // SharedPreferencesの状態をリセット
                    with(sharedPref2.edit()) {
                        putBoolean("btnStartDisabled", false)
                        putString("btnStartText", "捨てる") // ボタンのテキストを「捨てる」に戻す
                        apply()
                    }

                    // ボタンの状態を元に戻す
                    binding.btnStart.apply {
                        setBackgroundResource(R.drawable.design) // 元の背景に戻す
                        textSize = 80f // テキストサイズを設定
                        text = "捨てる" // テキストを「捨てる」に戻す
                        isEnabled = true // ボタンを有効にする
                    }
                }
            })
            timerFragment.startTimer()
        }
    }
    // タイマーを停止するメソッドを追加
    private fun stopQrUpdateTimer() {
        handler.removeCallbacksAndMessages(null)
    }

    // タイマーを開始するメソッド
    private fun startQrUpdateTimer() {
        handler.post(object : Runnable {
            override fun run() {
                updateQrCode()
                handler.postDelayed(this, updateInterval)
            }
        })
    }

    // QRコードを更新するメソッド
    private fun updateQrCode() {
        val randomData = QRCodeUtils.generateRandomFourDigitNumber().toString()
        val qrBitmap = QRCodeUtils.createQrCode(randomData)
        qrBitmap?.let {
            qrImage.setImageBitmap(it)
        }
    }
}
