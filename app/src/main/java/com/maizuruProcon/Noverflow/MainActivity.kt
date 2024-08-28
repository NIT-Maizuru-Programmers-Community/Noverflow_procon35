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


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets///apple apple
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
                hints // オプション
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

        val qrCode = createQrCode("https://www.maizuru-ct.ac.jp/")//ここを変える

        qrImage.setImageBitmap(qrCode)




    }
}

