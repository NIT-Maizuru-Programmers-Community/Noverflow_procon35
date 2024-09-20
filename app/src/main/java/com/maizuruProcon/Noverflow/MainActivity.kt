package com.maizuruProcon.Noverflow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.graphics.Bitmap
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.MultiFormatWriter
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.random.Random

import kotlin.concurrent.fixedRateTimer

class QR : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets///apple apple
        }

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
                200, // 生成されるイメージの高さ(px)
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

        //タイマー,QR更新
        var updateCount = 0
        val maxUpdates = 4 // 最大更新回数の設定

        val timer = fixedRateTimer("timer", false, 0L, 300000L) { // 300000ミリ秒（5分）ごとに実行
            if (updateCount < maxUpdates) {
                randomNumber = generateRandomFourDigitNumber()
                println("Random 4-digit number: $randomNumber")

                qrCode = createQrCode(randomNumber.toString())
                runOnUiThread {
                    qrImage.setImageBitmap(qrCode)
                }
                updateCount++
            } else {
                this.cancel()  // タイマーをキャンセル
            }
        }
    }
}

