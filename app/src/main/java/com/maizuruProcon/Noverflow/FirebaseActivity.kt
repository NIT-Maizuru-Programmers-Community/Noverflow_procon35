
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

fun getCollectionData(collectionName: String, onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance() // Firebaseが初期化されていることを前提
    db.collection(collectionName)
        .get()
        .addOnSuccessListener { documents ->
            onSuccess(documents)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}


