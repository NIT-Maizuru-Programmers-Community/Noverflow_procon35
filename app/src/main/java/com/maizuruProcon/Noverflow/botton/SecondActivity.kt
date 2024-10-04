package com.maizuruProcon.Noverflow.botton
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.maizuruProcon.Noverflow.R
import android.view.LayoutInflater
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.maizuruProcon.Noverflow.FourActivity
import com.maizuruProcon.Noverflow.MainActivity
import com.maizuruProcon.Noverflow.TimerService
import com.maizuruProcon.Noverflow.timer
import kotlin.random.Random
import android.view.ViewGroup


class SecondActivity : AppCompatActivity() {

    private var count=0
    private var count1=0
    private var count2=0
    private var count3=0
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //ボタンの取得
        val btnStart1: Button = findViewById(R.id.btnStart1)

        //ボタンを押したら次の画面へ
        btnStart1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
            finish()
       }
    }
}