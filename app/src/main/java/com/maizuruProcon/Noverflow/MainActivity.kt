package com.maizuruProcon.Noverflow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.graphics.Bitmap
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Color
import com.google.zxing.WriterException
import android.util.Log

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
    }
}

class QR : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ImageViewの参照を取得
        val qrCodeImage: ImageView = findViewById(R.id.qr_code_image)

        // アクティビティの起動時にQRコードを生成
        generateQRCode(qrCodeImage)
    }

    private fun generateQRCode(qrCodeImage: ImageView) {
        // QRコードに変換するURL
        val url = "https://www.maizuru-ct.ac.jp/"
        val writer = QRCodeWriter()
        try {
            // QRコードを生成
            val bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 200, 200)
            val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565)
            for (x in 0 until 200) {
                for (y in 0 until 200) {
                    // QRコードのピクセルを設定
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            // 生成したQRコードをImageViewに設定
            qrCodeImage.setImageBitmap(bitmap)
            // デバッグログを出力
            Log.d("QR", "QR code generated")
        } catch (e: WriterException) {
            // エラーが発生した場合のデバッグログを出力
            Log.e("QR", "QR code generation failed", e)
        }
    }
}