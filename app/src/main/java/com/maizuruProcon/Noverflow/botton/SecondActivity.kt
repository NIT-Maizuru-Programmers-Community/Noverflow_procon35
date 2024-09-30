package com.maizuruProcon.Noverflow.botton
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.maizuruProcon.Noverflow.R

import com.maizuruProcon.Noverflow.FourActivity


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //1)Viewの取得
        val btnStart1 :Button =findViewById(R.id.btnStart1)
        //2)ボタンを押したら次の画面へ
        btnStart1.setOnClickListener{
            val intent= Intent(this, FourActivity::class.java)
            startActivity(intent)
        }


        val btnBack :Button = findViewById(R.id.btnBack)
        //3)戻るボタン(アクティビティの終了)
        btnBack.setOnClickListener{
            finish()
       }

    }
}