package com.maizuruProcon.Noverflow.botton

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.maizuruProcon.Noverflow.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.maizuruProcon.Noverflow.MainActivity
import kotlin.random.Random
import com.maizuruProcon.Noverflow.KakuninActivity
import com.maizuruProcon.Noverflow.databinding.ActivitySecondBinding
import android.content.Context
import android.graphics.Color
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.maizuruProcon.Noverflow.QRCodeUtils


import java.io.ByteArrayOutputStream


class SecondActivity : AppCompatActivity() {

    private var count=0//瓶
    private var count1=0//缶
    private var count2=0//燃えるゴミ
    private var count3=0//ペットポトル
    private lateinit var binding: ActivitySecondBinding
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            // ボタンの見た目を更新
            btnStart1.apply {
                setBackgroundColor(Color.GRAY) // ボタンの背景を灰色にする
                text = "利用不可" // ボタンのテキストを「利用不可」に設定
                isEnabled = false // ボタンを無効にする
            }

            // 合計が0の場合の処理
            if (count + count1 + count2 + count3 == 0) {
                // KakuninActivityに移動するIntentを作成
                val intent = Intent(this, KakuninActivity::class.java)
                startActivity(intent)
            } else if (count + count1 + count2 + count3 > 0) {
                // ランダムな4桁の数字を生成
                val randomNumber = QRCodeUtils.generateRandomFourDigitNumber()
                println("ランダムな4桁の数字: $randomNumber")

                // ランダムな数字からQRコードを生成
                val qrCode = QRCodeUtils.createQrCode(randomNumber.toString())

                // QRコードのBitmapをIntentに渡す
                val intent = Intent(this, MainActivity::class.java)
                qrCode?.let {
                    val stream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    intent.putExtra("QR_CODE", byteArray)
                }
                startActivity(intent)
            }
        }


        val tv: TextView =findViewById(R.id.tv)
        val count1in: Button=findViewById(R.id.count1in)
        val count1re: Button=findViewById(R.id.count1re)

        count1in.setOnClickListener{
            count++
            tv.text=count.toString()
        }

        count1re.setOnClickListener{
            if(count>0) {
                count--
            }
            if(count<0){
                count==0
            }
            tv.text=count.toString()
        }

        val tv1: TextView =findViewById(R.id.tv1)
        val count2in: Button=findViewById(R.id.count2in)
        val count2re: Button=findViewById(R.id.count2re)

        count2in.setOnClickListener{
            count1++
            tv1.text=count1.toString()
        }

        count2re.setOnClickListener{
            if(count1>0) {
                count1--
            }
            if(count1<0){
                count1==0
            }
            tv1.text=count1.toString()
        }

        val tv2: TextView =findViewById(R.id.tv2)
        val count3in: Button=findViewById(R.id.count3in)
        val count3re: Button=findViewById(R.id.count3re)

        count3in.setOnClickListener{
            count2++
            tv2.text=count2.toString()
        }

        count3re.setOnClickListener{
            if(count2>0) {
                count2--
            }
            if(count2<0){
                count2==0
            }
            tv2.text=count2.toString()
        }

        val tv3: TextView =findViewById(R.id.tv3)
        val count4in: Button=findViewById(R.id.count4in)
        val count4re: Button=findViewById(R.id.count4re)

        count4in.setOnClickListener{
            count3++
            tv3.text=count3.toString()
        }

        count4re.setOnClickListener{
            if(count3>0) {
                count3--
            }
            if(count3<0){
                count3==0
            }
            tv3.text=count3.toString()
        }

        val btnBack :Button = findViewById(R.id.btnBack)
        //3)戻るボタン(アクティビティの終了)
        btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
       }
    }
}