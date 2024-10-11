package com.maizuruProcon.Noverflow

import Singleton
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.content.Context
import android.view.View

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

        } else if (garbage_kinds == 4){
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

        //合計の計算
        val all_total: Int =Singleton.moeru+Singleton.pet+Singleton.plastic+Singleton.kan

        //利用回数の表示
        val moeruTotalTextView: TextView = findViewById(R.id.moeru_total)
        moeruTotalTextView.text = "${Singleton.moeru}個"

        val petTotalTextView: TextView = findViewById(R.id.pet_total)
        petTotalTextView.text = "${Singleton.pet}個"

        val plasticTotalTextView: TextView = findViewById(R.id.plastic_total)
        plasticTotalTextView.text = "${Singleton.plastic}個"

        val kanTotalTextView: TextView = findViewById(R.id.kan_total)
        kanTotalTextView.text = "${Singleton.kan}個"

        // 今のレベルの表示
        val level_text: TextView = findViewById(R.id.textViewQuotient)
        level_text.text = "your level : ${Singleton.quotient}"

        //合計の表示
        val total_text: TextView = findViewById(R.id.all_total)
        total_text.text = "合計${all_total}個"

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

        //背景の制御
        // ImageViewの取得
        val back1: ImageView = findViewById(R.id.back1)
        val back2: ImageView = findViewById(R.id.back2)
        val back3: ImageView = findViewById(R.id.back3)
        val back4: ImageView = findViewById(R.id.back4)
        val back5: ImageView = findViewById(R.id.back5)
        val back6: ImageView = findViewById(R.id.back6)
        val back7: ImageView = findViewById(R.id.back7)
        val back8: ImageView = findViewById(R.id.back8)
        val back9: ImageView = findViewById(R.id.back9)
        val back10: ImageView = findViewById(R.id.back10)
        val back11: ImageView = findViewById(R.id.back11)
        val back12: ImageView = findViewById(R.id.back12)
        val back13: ImageView = findViewById(R.id.back13)
        val back14: ImageView = findViewById(R.id.back14)
        val back15: ImageView = findViewById(R.id.back15)

        //背景の設定
        if (Singleton.total <10){
            back1.visibility = View.GONE // 非表示
            back2.visibility = View.GONE // 非表示
            back3.visibility = View.GONE // 非表示
            back4.visibility = View.GONE // 非表示
            back5.visibility = View.GONE // 非表示
            back6.visibility = View.GONE // 非表示
            back7.visibility = View.GONE // 非表示
            back8.visibility = View.GONE // 非表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        } else if (10<= Singleton.total && Singleton.total < 20) {
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.GONE // 非表示
            back3.visibility = View.GONE // 非表示
            back4.visibility = View.GONE // 非表示
            back5.visibility = View.GONE // 非表示
            back6.visibility = View.GONE // 非表示
            back7.visibility = View.GONE // 非表示
            back8.visibility = View.GONE // 非表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        } else if(20<= Singleton.total && Singleton.total < 30){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.GONE // 非表示
            back4.visibility = View.GONE // 非表示
            back5.visibility = View.GONE // 非表示
            back6.visibility = View.GONE // 非表示
            back7.visibility = View.GONE // 非表示
            back8.visibility = View.GONE // 非表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(30<= Singleton.total && Singleton.total < 40){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.GONE // 非表示
            back5.visibility = View.GONE // 非表示
            back6.visibility = View.GONE // 非表示
            back7.visibility = View.GONE // 非表示
            back8.visibility = View.GONE // 非表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(40<= Singleton.total && Singleton.total < 50){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.GONE // 非表示
            back6.visibility = View.GONE // 非表示
            back7.visibility = View.GONE // 非表示
            back8.visibility = View.GONE // 非表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(50<= Singleton.total && Singleton.total < 60){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.GONE // 非表示
            back7.visibility = View.GONE // 非表示
            back8.visibility = View.GONE // 非表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(60<= Singleton.total && Singleton.total < 70){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.GONE // 非表示
            back8.visibility = View.GONE // 非表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(70<= Singleton.total && Singleton.total < 80){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.GONE // 非表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(80<= Singleton.total && Singleton.total < 90){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.VISIBLE // 表示
            back9.visibility = View.GONE // 非表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(90<= Singleton.total && Singleton.total < 100){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.VISIBLE // 表示
            back9.visibility = View.VISIBLE // 表示
            back10.visibility = View.GONE // 非表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(100<= Singleton.total && Singleton.total < 110){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.VISIBLE // 表示
            back9.visibility = View.VISIBLE // 表示
            back10.visibility = View.VISIBLE // 表示
            back11.visibility = View.GONE // 非表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(110<= Singleton.total && Singleton.total < 120){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.VISIBLE // 表示
            back9.visibility = View.VISIBLE // 表示
            back10.visibility = View.VISIBLE // 表示
            back11.visibility = View.VISIBLE // 表示
            back12.visibility = View.GONE // 非表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(120<= Singleton.total && Singleton.total < 130){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.VISIBLE // 表示
            back9.visibility = View.VISIBLE // 表示
            back10.visibility = View.VISIBLE // 表示
            back11.visibility = View.VISIBLE // 表示
            back12.visibility = View.VISIBLE // 表示
            back13.visibility = View.GONE // 非表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(130<= Singleton.total && Singleton.total < 140){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.VISIBLE // 表示
            back9.visibility = View.VISIBLE // 表示
            back10.visibility = View.VISIBLE // 表示
            back11.visibility = View.VISIBLE // 表示
            back12.visibility = View.VISIBLE // 表示
            back13.visibility = View.VISIBLE // 表示
            back14.visibility = View.GONE // 非表示
            back15.visibility = View.GONE // 非表示
        }else if(150<= Singleton.total){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.VISIBLE // 表示
            back9.visibility = View.VISIBLE // 表示
            back10.visibility = View.VISIBLE // 表示
            back11.visibility = View.VISIBLE // 表示
            back12.visibility = View.VISIBLE // 表示
            back13.visibility = View.VISIBLE // 表示
            back14.visibility = View.VISIBLE // 表示
            back15.visibility = View.GONE // 非表示
        }else if(Singleton.total >= 150){
            back1.visibility = View.VISIBLE // 表示
            back2.visibility = View.VISIBLE // 表示
            back3.visibility = View.VISIBLE // 表示
            back4.visibility = View.VISIBLE // 表示
            back5.visibility = View.VISIBLE // 表示
            back6.visibility = View.VISIBLE // 表示
            back7.visibility = View.VISIBLE // 表示
            back8.visibility = View.VISIBLE // 表示
            back9.visibility = View.VISIBLE // 表示
            back10.visibility = View.VISIBLE // 表示
            back11.visibility = View.VISIBLE // 表示
            back12.visibility = View.VISIBLE // 表示
            back13.visibility = View.VISIBLE // 表示
            back14.visibility = View.VISIBLE // 表示
            back15.visibility = View.VISIBLE // 表示
        }
    }
}