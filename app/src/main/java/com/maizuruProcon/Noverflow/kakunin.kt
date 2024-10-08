package com.maizuruProcon.Noverflow

import com.maizuruProcon.Noverflow.botton.SecondActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class KakuninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kakunin)

        val backbutton: Button = findViewById(R.id.backbutton)
        val homebutton: Button = findViewById(R.id.homebutton)

        // SecondActivityに戻るボタン
        backbutton.setOnClickListener {
            // Intentを作成してsecondActivityに遷移
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // MainActivityに戻るボタン
        homebutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}