package com.maizuruProcon.Noverflow.botton

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.maizuruProcon.Noverflow.R
import com.maizuruProcon.Noverflow.MainActivity
import com.maizuruProcon.Noverflow.KakuninActivity
import com.maizuruProcon.Noverflow.databinding.ActivitySecondBinding
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import updateFieldDataWithOption
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {

    private val counts = IntArray(4) // 各ゴミのカウントを格納する配列
    private lateinit var binding: ActivitySecondBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        // ボタンの取得
        val btnStart1: Button = findViewById(R.id.btnStart1)

        // ボタンを押したら次の画面へ
        btnStart1.setOnClickListener {

            // ボタンの状態をSharedPreferencesに保存
            val sharedPref = getSharedPreferences("ButtonState", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("btnStartDisabled", true)
                putString("btnStartText", "利用不可") // ボタンのテキストを「利用不可」に設定
                apply()
            }
            handleStartButtonClick(btnStart1)
        }

        // 各ゴミカウントのボタン設定
        setupCountButtons()

        // 戻るボタン
        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setupCountButtons() {
        val textViews = arrayOf(
            findViewById<TextView>(R.id.tv),
            findViewById<TextView>(R.id.tv1),
            findViewById<TextView>(R.id.tv2),
            findViewById<TextView>(R.id.tv3)
        )

        val incrementButtons = arrayOf(
            findViewById<Button>(R.id.count1in),
            findViewById<Button>(R.id.count2in),
            findViewById<Button>(R.id.count3in),
            findViewById<Button>(R.id.count4in)
        )

        val decrementButtons = arrayOf(
            findViewById<Button>(R.id.count1re),
            findViewById<Button>(R.id.count2re),
            findViewById<Button>(R.id.count3re),
            findViewById<Button>(R.id.count4re)
        )

        for (i in counts.indices) {
            val index = i
            incrementButtons[i].setOnClickListener {
                counts[index]++
                textViews[index].text = counts[index].toString()
            }

            decrementButtons[i].setOnClickListener {
                if (counts[index] > 0) {
                    counts[index]--
                }
                textViews[index].text = counts[index].toString()
            }
        }
    }

    private fun handleStartButtonClick(btnStart: Button) {
        // ボタンの状態をSharedPreferencesに保存
        val sharedPref = getSharedPreferences("ButtonState", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("btnStartDisabled", true)
            putString("btnStartText", "利用不可") // ボタンのテキストを「利用不可」に設定
            apply()
        }

        // 合計の計算
        val totalCount = counts.sum()
        if (totalCount == 0) {
            startActivity(Intent(this, KakuninActivity::class.java))
        } else if (totalCount > 0) {
            val randomNumber = generateRandomFourDigitNumber()
            println("Random 4-digit number: $randomNumber")

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
            val data = mapOf(
                "burningGarbage" to counts[0],
                "plasticGarbage" to counts[1],
                "bottles" to counts[2],
                "cans" to counts[3]
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

            // QRコードを生成
            val qrCode = createQrCode(randomNumber)

            // QRコードをBitmapとしてIntentに渡す
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("QR_CODE", qrCode)
            startActivity(intent)
        }
    }

    private fun generateRandomFourDigitNumber(): String {
        return String.format("%04d", Random.nextInt(0, 10000))
    }

    private fun createBitMatrix(data: String): BitMatrix? {
        val multiFormatWriter = MultiFormatWriter()
        val hints = mapOf(
            EncodeHintType.MARGIN to 0,
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L
        )

        return multiFormatWriter.encode(
            data,
            BarcodeFormat.QR_CODE,
            170,
            200,
            hints
        )
    }

    private fun createBitmap(bitMatrix: BitMatrix): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)
    }

    private fun createQrCode(data: String): Bitmap? {
        return try {
            val bitMatrix = createBitMatrix(data)
            bitMatrix?.let { createBitmap(it) }
        } catch (e: Exception) {
            null
        }
    }
}
