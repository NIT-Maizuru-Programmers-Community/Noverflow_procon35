import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object FirestoreUtils {
    private lateinit var listenerRegistration: ListenerRegistration

    // フラグの変更を監視するためのリスナーを設定
    fun listenToFlagChanges(onFlagChanged: (Boolean, String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("garbageBoxes")  // コレクション名を指定

        // リアルタイムリスナーを設定
        listenerRegistration = collectionRef.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.e("firestore", "Error fetching documents: ", e)
                return@addSnapshotListener
            }

            if (snapshots != null && !snapshots.isEmpty) {
                for (document in snapshots.documents) {
                    if (document.exists()) {
                        val flag = document.getBoolean("flag") ?: false  // デフォルトはfalse
                        val documentId = document.id  // ドキュメントIDを取得

                        // ログにドキュメントIDとflagの値を出力
                        Log.d("firestore", "Document ID: $documentId, Flag: $flag")

                        // flagがtrueに変更された場合の処理
                        if (flag) {
                            Log.d("firestore", "Flag is true for Document ID: $documentId")
                            onFlagChanged(flag, documentId) // 引数の順序を修正
                        }
                    }
                }
            } else {
                Log.d("firestore", "No documents in the collection")
            }
        }
    }

    fun stopListeningToFlagChanges() {
        listenerRegistration.remove()
    }
}
