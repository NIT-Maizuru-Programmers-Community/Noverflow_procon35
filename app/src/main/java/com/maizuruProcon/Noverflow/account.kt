package com.maizuruProcon.Noverflow

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.content.Context

class account: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_layout)

        // ボタンを取得
        val backButton: ImageButton = findViewById(R.id.backbutton)
        val imageView: ImageView = findViewById(R.id.level_bar)
        val resetButton: Button = findViewById(R.id.resetButton2)

        // ボタンが押された時の処理
        backButton.setOnClickListener {
            // 現在のアクティビティを終了して前の画面に戻る
            finish()
        }

        //レベルバー,レベルの計算

        // SharedPreferencesからデータを読み込む
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        Singleton.total = sharedPref.getInt("total", 0)
        Singleton.moeru = sharedPref.getInt("moeru", 0)
        Singleton.pet = sharedPref.getInt("pet", 0)
        Singleton.plastic = sharedPref.getInt("plastic", 0)
        Singleton.kan = sharedPref.getInt("kan", 0)

        // 3で割った余りを計算（レベルバーの値）
        Singleton.remainder = Singleton.total % 3

        // 3で割ったときの商を計算（レベルの値）
        Singleton.quotient = Singleton.total / 3

        // 余りの値に基づいて画像を変更
        if (Singleton.remainder == 3 || Singleton.remainder == 0) {
            imageView.setImageResource(R.drawable.count1)  // 余りが0,3の時の画像
        } else if (Singleton.remainder == 1) {
            imageView.setImageResource(R.drawable.count2)  // 余りが1の時の画像
        } else if (Singleton.remainder == 2) {
            imageView.setImageResource(R.drawable.count3)  // 余りが2の時の画像
        }

        // textViewQuotientに今のレベルを表示
        val textView: TextView = findViewById(R.id.textViewQuotient)
        textView.text = "your level : ${Singleton.quotient}"


        //仮の値（分別の結果を受け取る）
        val garbage_kinds: Int = 1

        //捨てたごみの種類別カウント
        if (garbage_kinds == 1) {
            Singleton.moeru += 1

            with(sharedPref.edit()) {
                putInt("moeru", Singleton.moeru)
                apply()
            }

        } else if (garbage_kinds == 2) {
            Singleton.pet += 1

            with(sharedPref.edit()) {
                putInt("pet", Singleton.pet)
                apply()
            }

        } else if (garbage_kinds == 3) {
            Singleton.plastic += 1

            with(sharedPref.edit()) {
                putInt("plastic", Singleton.plastic)
                apply()
            }

        } else {
            Singleton.kan += 1

            with(sharedPref.edit()) {
                putInt("kan", Singleton.kan)
                apply()
            }

        }

        // SharedPreferencesからデータを読み込む
        Singleton.moeru = sharedPref.getInt("moeru", 0)
        Singleton.pet = sharedPref.getInt("pet", 0)
        Singleton.plastic = sharedPref.getInt("plastic", 0)
        Singleton.kan = sharedPref.getInt("kan", 0)

        //利用回数の表示
        val moeruTotalTextView: TextView = findViewById(R.id.moeru_total)
        moeruTotalTextView.text = "${Singleton.moeru}回"

        val petTotalTextView: TextView = findViewById(R.id.pet_total)
        petTotalTextView.text = "${Singleton.pet}回"

        val plasticTotalTextView: TextView = findViewById(R.id.plastic_total)
        plasticTotalTextView.text = "${Singleton.plastic}回"

        val kanTotalTextView: TextView = findViewById(R.id.kan_total)
        kanTotalTextView.text = "${Singleton.kan}回"

        //リセットボタンの実装
        resetButton.setOnClickListener {
            Singleton.moeru = 0
            Singleton.pet = 0
            Singleton.plastic = 0
            Singleton.kan = 0

            // SharedPreferencesにデータを保存
            with(sharedPref.edit()) {
                putInt("moeru", Singleton.moeru)
                putInt("pet", Singleton.pet)
                putInt("plastic", Singleton.plastic)
                putInt("kan", Singleton.kan)
                apply()
            }
        }

    }
}