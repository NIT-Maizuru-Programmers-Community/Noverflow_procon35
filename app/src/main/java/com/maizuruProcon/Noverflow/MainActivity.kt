package com.maizuruProcon.Noverflow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ListenerRegistration
import getCollectionData
import getDocumentData
import getFieldData
import updateFieldDataWithOption

class MainActivity : AppCompatActivity() {

    // リスナー登録を保持するための変数
    private lateinit var listenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        /*listenerRegistration = getCollectionData(
            collectionName = "garbageBoxes", // コレクション名
            onSuccess = { documents -> // 成功時の処理
                for (document in documents) {
                    Log.d("Firestore", "${document.id} => ${document.data}")
                }
            },
            onFailure = { exception -> // 失敗時の処理
                Log.e("Firestore", "Error getting documents: ", exception)
            }
        )*/

        /*listenerRegistration = getDocumentData(
            collectionName = "garbageBoxes",  // コレクション名
            documentId = "maizuruCollege",    // ドキュメントID
            onSuccess = { documentData ->     // 成功時の処理
                // ドキュメントのデータをログに表示
                Log.d("Firestore", "Document data: $documentData")
            },
            onFailure = { exception ->        // 失敗時の処理
                Log.e("Firestore", "Error getting document: ", exception)
            }
        )*/

        /*listenerRegistration = getFieldData(
            collectionName = "garbageBoxes",  // コレクション名
            documentId = "maizuruCollege",    // ドキュメントID
            fieldName = "location",      // 取得したいフィールド名
            onSuccess = { fieldValue ->       // 成功時の処理
                Log.d("Firestore", "Field value: $fieldValue") // フィールドの値をログに表示
            },
            onFailure = { exception ->        // 失敗時の処理
                Log.e("Firestore", "Error getting field: ", exception)
            }
        )*/

        updateFieldDataWithOption(
            collectionName = "garbageBoxes",
            documentId = "maizuruCollege",
            fieldName = "trash",
            value = 100,
            updateMode = UpdateMode.INCREMENT,
            onSuccess = {
                Log.d("Firestore", "Field updated successfully")
            },
            onFailure = { exception ->
                Log.e("Firestore", "Error updating field", exception)
            }
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration.remove()
    }
}
