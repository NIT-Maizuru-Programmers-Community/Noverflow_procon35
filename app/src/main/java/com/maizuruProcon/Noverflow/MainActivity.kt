package com.maizuruProcon.Noverflow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import getCollectionData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        getCollectionData(
            collectionName = "garbageBoxes", // コレクション名を指定
            onSuccess = { documents -> // 成功時の処理
                for (document in documents) {
                    // 各ドキュメントのIDとデータをログに表示
                    Log.d("Firestore", "${document.id} => ${document.data}")
                }
            },
            onFailure = { exception -> // 失敗時の処理
                Log.e("Firestore", "Error getting documents: ", exception)
            }
        )
    }
}
