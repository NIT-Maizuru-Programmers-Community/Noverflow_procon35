package com.maizuruProcon.Noverflow.botton

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ListenerRegistration
import com.maizuruProcon.Noverflow.QRCodeUtils.generateRandomFourDigitNumber
import com.maizuruProcon.Noverflow.GarbageViewModel
import com.maizuruProcon.Noverflow.QRCodeUtils.createQrCode
import updateFieldDataWithOption

class SecondActivity : AppCompatActivity() {

    private val counts = IntArray(4) // 各ゴミのカウントを格納する配列
    private lateinit var binding: ActivitySecondBinding
    private lateinit var listenerRegistration: ListenerRegistration
    private lateinit var garbageViewModel: GarbageViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        garbageViewModel = ViewModelProvider(this).get(GarbageViewModel::class.java)


        val btnStart1: Button = findViewById(R.id.btnStart1) // ボタンの取得
        btnStart1.setOnClickListener { // ボタンを押したら次の画面へ
            handleStartButtonClick(btnStart1)
        }

        setupCountButtons()// 各ゴミカウントのボタン設定

        // 戻るボタン
        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        garbageViewModel.counts.removeObservers(this) // ViewModelのオブザーバーも解除
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

            val selectedCounts = mapOf(
                "burningGarbage" to counts[0],
                "plasticGarbage" to counts[1],
                "bottles" to counts[2],
                "cans" to counts[3]
            )
            garbageViewModel.updateCounts(selectedCounts)

            val qrCode = createQrCode(randomNumber)// QRコードを生成

            val intent = Intent(this, MainActivity::class.java)// QRコードをBitmapとしてIntentに渡す
            intent.putExtra("QR_CODE", qrCode)
            startActivity(intent)
        }
    }
}
