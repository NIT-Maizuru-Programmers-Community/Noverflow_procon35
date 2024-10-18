import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object FirestoreUtils {
    private var listenerRegistration: ListenerRegistration? = null

    // フラグの変更を監視するためのリスナーを設定
    fun listenToFlagChanges(onFlagChanged: (Boolean, String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("garbageBoxes")

        listenerRegistration = collectionRef.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.e("Firestore", "Error fetching documents: ", e)
                return@addSnapshotListener
            }

            if (snapshots != null && !snapshots.isEmpty) {
                for (document in snapshots.documents) {
                    val flag = document.getBoolean("flag") ?: false
                    val documentId = document.id
                    Log.d("Firestore", "Document ID: $documentId, Flag: $flag")

                    // フラグが true の場合にコールバックを実行
                    if (flag) {
                        onFlagChanged(flag, documentId)
                    }
                }
            } else {
                Log.d("Firestore", "No documents found in the collection")
            }
        }
    }

    // リスナーを停止するメソッド
    fun stopListeningToFlagChanges() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }
}
