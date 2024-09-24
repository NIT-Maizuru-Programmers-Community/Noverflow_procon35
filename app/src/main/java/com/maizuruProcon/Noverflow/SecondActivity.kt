package com.maizuruProcon.Noverflow
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        val btnBack :Button = findViewById(R.id.btnBack)
        //3)戻るボタン(アクティビティの終了)
        btnBack.setOnClickListener{
            finish()
        }

    }
}