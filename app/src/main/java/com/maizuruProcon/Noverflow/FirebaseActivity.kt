import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

fun getCollectionData(
    collectionName: String,
    onSuccess: (QuerySnapshot) -> Unit,
    onFailure: (Exception) -> Unit
): ListenerRegistration {
    val db = FirebaseFirestore.getInstance()
    return db.collection(collectionName)
        .addSnapshotListener { snapshots, exception ->
            if (exception != null) {
                onFailure(exception)
                return@addSnapshotListener
            }

            if (snapshots != null) {
                onSuccess(snapshots)
            }
        }
}

fun getDocumentData(
    collectionName: String,
    documentId: String,
    onSuccess: (Map<String, Any>?) -> Unit,
    onFailure: (Exception) -> Unit
): ListenerRegistration {
    val db = FirebaseFirestore.getInstance()
    val documentRef = db.collection(collectionName).document(documentId)

    return documentRef.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            onFailure(exception)
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
            onSuccess(snapshot.data)
        } else {
            onSuccess(null)
        }
    }
}

fun getFieldData(
    collectionName: String,
    documentId: String,
    fieldName: String,
    onSuccess: (Any?) -> Unit,
    onFailure: (Exception) -> Unit
): ListenerRegistration {
    val db = FirebaseFirestore.getInstance() // Firebaseが初期化されていることを前提
    val documentRef = db.collection(collectionName).document(documentId)

    // ドキュメントリスナーの登録
    return documentRef.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            onFailure(exception) // エラー時の処理
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
            // 特定のフィールドのデータを取得
            val fieldValue = snapshot.get(fieldName)
            onSuccess(fieldValue) // フィールドの値を返す
        } else {
            onSuccess(null) // ドキュメントが存在しない場合
        }
    }
}

fun getMapFieldValueSum(
    collectionName: String,
    documentId: String,
    fieldName: String,
    onResult: (Int?) -> Unit  // 合計値を返すコールバック
) {
    getFieldData(
        collectionName = collectionName,
        documentId = documentId,
        fieldName = fieldName,
        onSuccess = { fieldValue ->
            // フィールドがMapであるかを確認
            val mapField = fieldValue as? Map<String, Number>
            if (mapField != null) {
                // Mapの値の合計を計算
                val total = mapField.values.sumOf { it.toInt() }
                Log.d("Firestore", "合計値: $total")
                onResult(total)  // 合計値を返す
            } else {
                Log.e("Firestore", "フィールドがMap型ではありません")
                onResult(null)  // 失敗時にはnullを返す
            }
        },
        onFailure = { exception ->
            Log.e("Firestore", "Error getting field: ", exception)
            onResult(null)  // 失敗時にはnullを返す
        }
    )
}

enum class UpdateMode {
    SET,      // 上書き
    INCREMENT // 加算
}

// ドキュメント内の特定フィールドを更新または加算する関数
fun updateFieldDataWithOption(
    collectionName: String,     // コレクション名
    documentId: String,         // ドキュメントID
    fieldName: String,          // 更新したいフィールド名
    value: Any,                 // 新しいフィールドの値
    updateMode: UpdateMode,     // 更新モード (上書きか加算か)
    onSuccess: () -> Unit,      // 成功時の処理
    onFailure: (Exception) -> Unit // 失敗時の処理
) {
    val db = FirebaseFirestore.getInstance() // Firebaseが初期化されていることを前提
    val documentRef = db.collection(collectionName).document(documentId)

    when (updateMode) {
        UpdateMode.SET -> {
            // フィールドを指定された値で上書き
            documentRef.update(fieldName, value)
                .addOnSuccessListener {
                    onSuccess() // 成功時の処理を呼び出す
                }
                .addOnFailureListener { exception ->
                    onFailure(exception) // 失敗時の処理を呼び出す
                }
        }
        UpdateMode.INCREMENT -> {
            // フィールドの値を加算
            if (value is Number) { // 加算は数値にのみ適用可能
                documentRef.update(fieldName, FieldValue.increment(value.toDouble()))
                    .addOnSuccessListener {
                        onSuccess() // 成功時の処理を呼び出す
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception) // 失敗時の処理を呼び出す
                    }
            } else {
                onFailure(IllegalArgumentException("Increment requires a numeric value"))
            }
        }
    }
}
