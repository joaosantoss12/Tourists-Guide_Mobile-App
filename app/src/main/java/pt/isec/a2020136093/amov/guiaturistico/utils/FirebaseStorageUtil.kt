package pt.isec.a2020136093.amov.guiaturistico.utils

import android.content.res.AssetManager
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel
import java.io.IOException
import java.io.InputStream

class FirebaseStorageUtil {
    companion object {
        fun addDataToFirestore(onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore

            val scores = hashMapOf(
                "nrgames" to 0,
                "topscore" to 0
            )
            db.collection("Scores").document("Level1").set(scores)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }

        fun updateDataInFirestore(onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val v = db.collection("Scores").document("Level1")

            v.get(Source.SERVER)
                .addOnSuccessListener {
                    val exists = it.exists()
                    Log.i("Firestore", "updateDataInFirestore: Success? $exists")
                    if (!exists) {
                        onResult(Exception("Doesn't exist"))
                        return@addOnSuccessListener
                    }
                    val value = it.getLong("nrgames") ?: 0
                    v.update("nrgames", value + 1)
                    onResult(null)
                }
                .addOnFailureListener { e ->
                    onResult(e)
                }
        }

        fun updateDataInFirestoreTrans(onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val v = db.collection("Scores").document("Level1")

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    val newnrgames = (doc.getLong("nrgames") ?: 0) + 1
                    val newtopscore = (doc.getLong("topscore") ?: 0) + 100
                    transaction.update(v, "nrgames", newnrgames)
                    transaction.update(v, "topscore", newtopscore)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }

        fun removeDataFromFirestore(onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val v = db.collection("Scores").document("Level1")

            v.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }

        private var listenerRegistration: ListenerRegistration? = null

        fun startObserver(onNewValues: (Long, Long) -> Unit) {
            stopObserver()
            val db = Firebase.firestore
            listenerRegistration = db.collection("Scores").document("Level1")
                .addSnapshotListener { docSS, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (docSS != null && docSS.exists()) {
                        val nrgames = docSS.getLong("nrgames") ?: 0
                        val topscore = docSS.getLong("topscore") ?: 0
                        Log.i("Firestore", "$nrgames : $topscore")
                        onNewValues(nrgames, topscore)
                    }
                }
        }

        fun stopObserver() {
            listenerRegistration?.remove()
        }


// Storage

        fun getFileFromAsset(assetManager: AssetManager, strName: String): InputStream? {
            var istr: InputStream? = null
            try {
                istr = assetManager.open(strName)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return istr
        }

//https://firebase.google.com/docs/storage/android/upload-files

        fun uploadFile(inputStream: InputStream, imgFile: String) {
            val storage = Firebase.storage
            val ref1 = storage.reference
            val ref2 = ref1.child("images")
            val ref3 = ref2.child(imgFile)

            val uploadTask = ref3.putStream(inputStream)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref3.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    println(downloadUri.toString())
                } else {
                    // Handle failures
                    // ...
                }
            }


        }

        fun getLocations() {
            val db = Firebase.firestore

            db.collection("Localidades").get()
                .addOnSuccessListener { result ->
                    val localidades = mutableListOf<Triple<String, String, String>>()
                    for (document in result) {
                        localidades.add(
                            Triple(
                                document.data["nome"].toString(),
                                document.data["descrição"].toString(),
                                document.data["imagemURL"].toString()
                            )
                        )
                    }
                    FirebaseViewModel._locations.value = localidades
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }

        fun getLocaisInteresse() {
            val db = Firebase.firestore

            Log.i("OAIJGIA", "CIDADE: " + FirebaseViewModel.currentLocation.value)

            db.collection("Localidades").document(FirebaseViewModel.currentLocation.value.toString()).collection("Locais de Interesse").get()
                .addOnSuccessListener { result ->
                    val locaisInteresse = mutableListOf<Pair<Triple<String, String, String>, Triple<String, Any?, Any?>>>()
                    for (document in result) {
                        locaisInteresse.add(
                            Pair(
                                Triple(
                                    document.data["nome"].toString(),
                                    document.data["descrição"].toString(),
                                    document.data["imagemURL"].toString()
                                ),
                                Triple(
                                    document.data["categoria"].toString(),
                                    document.data["classificação"],
                                    document.data["coordenadas"]
                                )
                            ),
                        )
                    }
                    FirebaseViewModel._locaisInteresse.value = locaisInteresse
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }
    }
}