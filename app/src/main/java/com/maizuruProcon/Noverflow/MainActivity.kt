package com.maizuruProcon.Noverflow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

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

//このクラスが上手く出来てなさそうです
class QRcode : AppCompatActivity() {

    private lateinit var qrCodeImageView: ImageView
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 1200000 // 20分をミリ秒で指定

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // qrCodeImageViewは、レイアウトファイルに定義されている既存のImageViewです。
        qrCodeImageView = findViewById(R.id.qrCodeImageView)
        updateQRCode()
    }

    private fun updateQRCode() {
        // 仮のQRコードの内容。後で変更可能です。
        val content = "https://example.com"
        val bitmap = generateQRCode(content)
        qrCodeImageView.setImageBitmap(bitmap)

        // 指定された時間間隔でQRコードを更新する
        handler.postDelayed({ updateQRCode() }, updateInterval)
    }

    private fun generateQRCode(content: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 148, 152)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                // QRコードのビットマトリックスをビットマップに変換
                bmp.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
            }
        }
        return bmp
    }
}