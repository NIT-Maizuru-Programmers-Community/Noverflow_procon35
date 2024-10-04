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
import com.google.firebase.inappmessaging.MessagesProto
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.MultiFormatWriter
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.random.Random
import kotlin.concurrent.fixedRateTimer
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.content.Context
import android.content.BroadcastReceiver
import android.content.IntentFilter
import com.maizuruProcon.Noverflow.botton.SecondActivity


class MainActivity : AppCompatActivity() {

    private lateinit var timerFinishedReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets///apple apple
        }
        //1)Viewの取得
        val btnStart: Button = findViewById(R.id.btnStart)
        //2)ボタンを押したら次の画面へ
        btnStart.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // ボタンを取得
        val imageButton: ImageButton = findViewById(R.id.button)
        val button: Button = findViewById(R.id.test)
        val resetButton: Button = findViewById(R.id.resetButton)

        // ボタンが押された時の処理(画面遷移)
        imageButton.setOnClickListener {
            // Intentを作成してaccountActivityに遷移
            val intent = Intent(this, account::class.java)
            startActivity(intent)
        }


        // ごみを捨てた回数のカウント
        //仮のボタンを押したら+1

        // SharedPreferencesの読み込み
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        Singleton.total = sharedPref.getInt("total", 0)
        Singleton.moeru = sharedPref.getInt("moeru", 0)
        Singleton.pet = sharedPref.getInt("pet", 0)
        Singleton.plastic = sharedPref.getInt("plastic", 0)
        Singleton.kan = sharedPref.getInt("kan", 0)


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


        //QRの生成と更新、タイマーの表示
        fun generateRandomFourDigitNumber(): Int {
            return Random.nextInt(1000, 10000)
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

        val qrImage: ImageView = findViewById(R.id.qr_code_image)

        // アプリケーションの起動時にランダムな4桁の数字を生成
        var randomNumber: Int = generateRandomFourDigitNumber()
        println("Random 4-digit number: $randomNumber")

        // QRコードを生成
        var qrCode = createQrCode(randomNumber.toString())
        qrImage.setImageBitmap(qrCode)

        // Fragment: timerの使用
        val fragment = timer()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        // タイマーでQR更新
        var updateCount = 0
        val maxUpdates = 5 // 最大更新回数の設定(5)

        // サービスのインテントを作成し、タイマーをバックグラウンドで実行
        val intent = Intent(this, TimerService::class.java)
        startService(intent) // サービスを開始

        // タイマー終了時にQRコード更新と処理を行うためのBroadcastReceiverを登録
        timerFinishedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (updateCount < maxUpdates) {
                    randomNumber = generateRandomFourDigitNumber()
                    println("Random 4-digit number: $randomNumber")

                    qrCode = createQrCode(randomNumber.toString())
                    runOnUiThread {
                        qrImage.setImageBitmap(qrCode)
                    }
                    updateCount++
                } else {
                    // QRコードの更新終了時の処理
                    Handler(Looper.getMainLooper()).postDelayed({
                        runOnUiThread {
                            qrImage.setImageBitmap(null) // QRコードをクリア
                            qrImage.setBackgroundResource(R.drawable.qr_code_border) // デフォルトの背景画像に戻す
                        }
                    }, 5*60*1000L) // 300000ミリ秒（5分後に画像をクリア）
                }
            }
        }

        // BroadcastReceiverを登録
        registerReceiver(timerFinishedReceiver, IntentFilter("TIMER_FINISHED"))
    }

    // Activity/Fragmentが終了する際にBroadcastReceiverを解除
    override fun onDestroy() {
        unregisterReceiver(timerFinishedReceiver)
        super.onDestroy()
    }
}