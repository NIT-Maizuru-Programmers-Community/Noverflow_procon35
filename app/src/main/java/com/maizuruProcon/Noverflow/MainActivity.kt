package com.maizuruProcon.Noverflow

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Bitmap
import android.widget.Button
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.MultiFormatWriter
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.random.Random
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.content.Context
import android.content.BroadcastReceiver
import android.content.IntentFilter
import com.maizuruProcon.Noverflow.botton.SecondActivity
import android.graphics.Color
import com.maizuruProcon.Noverflow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var timerFinishedReceiver: BroadcastReceiver
    private var updateCount = 0
    private val maxUpdates = 5 // 最大更新回数の設定(5)
    private lateinit var timerFragment: TimerFragment
    private lateinit var binding: ActivityMainBinding

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
        val sharedPref2 = getSharedPreferences("ButtonState", Context.MODE_PRIVATE)
        val isBtnStartDisabled = sharedPref2.getBoolean("btnStartDisabled", false)
        val btnStartText = sharedPref2.getString("btnStartText", "捨てる")

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
        val btnstart: Button = findViewById(R.id.btnStart)
        val mapButton :Button =findViewById(R.id.mapButton)

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
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        Singleton.total = sharedPref.getInt("total", 0)
        Singleton.moeru = sharedPref.getInt("moeru", 0)
        Singleton.pet = sharedPref.getInt("pet", 0)
        Singleton.plastic = sharedPref.getInt("plastic", 0)
        Singleton.kan = sharedPref.getInt("kan", 0)

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
            with(sharedPref.edit()) {
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

        // ImageViewの取得
        val qrImage: ImageView = findViewById(R.id.qr_code_image)

        // IntentからQRコードを取得
        val qrCode: Bitmap? = intent.getParcelableExtra("QR_CODE")

        // タイマーのフラグメントを追加
        timerFragment = TimerFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, timerFragment)
            .commit()

        // QRコードをImageViewに設定
        qrCode?.let {
            qrImage.setImageBitmap(it)
            timerFragment.startTimer() // QRコードが生成された後にタイマーを起動
        }

        // タイマー終了時にQRコード更新と処理を行うためのBroadcastReceiverを登録
        timerFinishedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (updateCount < maxUpdates) {
                    val randomNumber = generateRandomFourDigitNumber()
                    println("Random 4-digit number: $randomNumber")

                    val qrCode = createQrCode(randomNumber.toString())
                    runOnUiThread {
                        qrImage.setImageBitmap(qrCode)
                    }
                    updateCount++
                    timerFragment.startTimer() // タイマーを再起動
                }
            }
        }

        // BroadcastReceiverを登録
        registerReceiver(timerFinishedReceiver, IntentFilter("TIMER_FINISHED"),
            RECEIVER_NOT_EXPORTED
        )

        // サービスのインテントを作成し、タイマーをバックグラウンドで実行
        val intentService = Intent(this, TimerService::class.java)
        startService(intentService) // サービスを開始

        // タイマーが終了したらボタンの状態を元に戻す
        Handler(Looper.getMainLooper()).postDelayed({
            runOnUiThread {
                qrImage.setImageBitmap(null) // QRコードをクリア
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
        }, 1 * 60 * 1000L) // 5分後に画像とボタンの状態をリセット
    }

    fun generateRandomFourDigitNumber(): Int {
        return Random.nextInt(1000, 9999)
    }

    fun createBitMatrix(data: String): BitMatrix? {
        val multiFormatWriter = MultiFormatWriter()
        val hints = mapOf(
            // マージン
            EncodeHintType.MARGIN to 0,
            // 誤り訂正レベルを一番低いレベルで設定 エンコード対象のデータ量が少ないため
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L
        )

        return multiFormatWriter.encode(
            data, // QRコード化したいデータ
            BarcodeFormat.QR_CODE, // QRコードにしたい場合はこれを指定
            170, // 生成されるイメージの高さ(px)
            200, // 生成されるイメージの横幅(px)
            hints
        )
    }

    fun createBitmap(bitMatrix: BitMatrix): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)
    }

    fun createQrCode(data: String): Bitmap? {
        return try {
            val bitMatrix = createBitMatrix(data)
            bitMatrix?.let { createBitmap(it) }
        } catch (e: Exception) {
            null
        }
    }

    // Activity/Fragmentが終了する際にBroadcastReceiverを解除
    override fun onDestroy() {
        unregisterReceiver(timerFinishedReceiver)
        super.onDestroy()
    }
}
