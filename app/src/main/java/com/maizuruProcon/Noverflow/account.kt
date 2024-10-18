package com.maizuruProcon.Noverflow

import Singleton
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.util.Log
import android.view.View
import getFieldData
import getMapFieldValueSum

class account: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_layout)

        val counts = IntArray(4) // 各ゴミのカウントを格納する配列

        // ボタンを取得
        val backButton: ImageButton = findViewById(R.id.backbutton)
        val imageView: ImageView = findViewById(R.id.level_bar)

        backButton.setOnClickListener {// ボタンが押された時の処理
            finish()// 現在のアクティビティを終了して前の画面に戻る
        }

        var total: Int?
        getMapFieldValueSum(// ごみを捨てた回数のカウント
            collectionName = "noverflow-apps",  // コレクション名
            documentId = "pixel4a",    // ドキュメントID
            fieldName = "garbages"      // 取得したいフィールド名
        ) { result ->
            total = result
            total?.let {
                Log.d("Firestore", "取得した合計値: $total")

                val remainder = it % 3
                val quotient = it / 3
                updateImage(remainder, imageView)// 余りの値に基づいて画像を変更

                val level_text: TextView = findViewById(R.id.textViewQuotient)//今のレベルの表示
                level_text.text = "Your Level : ${quotient}"

                val total_text: TextView = findViewById(R.id.all_total)//合計の表示
                total_text.text = "合計${total}個"
            } ?: run {
                Log.e("Firestore", "合計値の取得に失敗しました")
            }
        }

        val data = mapOf(
            "burningGarbage" to counts[0],
            "plasticGarbage" to counts[1],
            "bottles" to counts[2],
            "cans" to counts[3]
        )

        for ((key) in data) {
            getFieldData(
                collectionName = "noverflow-apps",
                documentId = "pixel4a",
                fieldName = "garbages.$key",  // フィールド名を "garbages.<key>" に

                onSuccess = { fieldValue ->  // 成功時の処理
                    Log.d("Firestore", "$key field value: $fieldValue")

                    // 取得した値をInt型に変換
                    val valueAsInt = when (fieldValue) {
                        is Long -> fieldValue.toInt()  // Longの場合
                        is Double -> fieldValue.toInt()  // Doubleの場合（小数点切り捨て）
                        else -> 0  // 値が取得できなかった場合のデフォルト値
                    }

                    // TextViewに反映
                    when (key) {
                        "burningGarbage" -> {
                            val moeruTotalTextView: TextView = findViewById(R.id.moeru_total)
                            moeruTotalTextView.text = "$valueAsInt 個"
                        }
                        "plasticGarbage" -> {
                            val plasticTotalTextView: TextView = findViewById(R.id.plastic_total)
                            plasticTotalTextView.text = "$valueAsInt 個"
                        }
                        "bottles" -> {
                            val petTotalTextView: TextView = findViewById(R.id.pet_total)
                            petTotalTextView.text = "$valueAsInt 個"
                        }
                        "cans" -> {
                            val kanTotalTextView: TextView = findViewById(R.id.kan_total)
                            kanTotalTextView.text = "$valueAsInt 個"
                        }
                    }
                },
                onFailure = { exception ->  // 失敗時の処理
                    Log.e("Firestore", "Error getting field: ", exception)
                }
            )
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
    private fun updateImage(remainder: Int?, imageView: ImageView) {// 画像を更新する関数
        when (remainder) {
            0, 3 -> imageView.setImageResource(R.drawable.count1)  // 数字が0,3の時の画像
            1 -> imageView.setImageResource(R.drawable.count2)  // 数字が1の時の画像
            2 -> imageView.setImageResource(R.drawable.count3)  // 数字が2の時の画像
        }
    }
}