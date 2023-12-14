package pt.isec.a2020136093.amov.guiaturistico.utils

import android.content.res.AssetManager
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocalInteresse
import pt.isec.a2020136093.amov.guiaturistico.viewModel.Localizacao
import java.io.File
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

        fun uploadFile(imagePath: String) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val file = Uri.fromFile(File(imagePath))
            val uploadTask = file?.let { storageRef.child("${it.lastPathSegment}").putFile(it) }

            // Register observers to listen for when the download is done or if it fails
            uploadTask?.addOnFailureListener {

            }?.addOnSuccessListener { taskSnapshot ->

            }
        }

        fun getImageURL(imagePath: String): String {
            val storage = Firebase.storage

            val storageRef = storage.reference
            val file = Uri.fromFile(File(imagePath))
            val imageRef = storageRef.child(file.lastPathSegment.toString())

            val url = imageRef.downloadUrl.result.toString()

            return url
        }

        fun getLocations() {
            val db = Firebase.firestore

            db.collection("Localidades").get()
                .addOnSuccessListener { result ->
                    val localidades = mutableListOf<Localizacao>()
                    for (document in result) {
                        //if (document.data["estado"].toString() == "aprovado") {
                            localidades.add(
                                Localizacao(
                                    document.data["nome"].toString(),
                                    document.data["descrição"].toString(),
                                    document.data["imagemURL"].toString(),
                                    document.data["coordenadas"].toString(),
                                    document.data["email"].toString(),
                                    document.data["estado"].toString(),
                                    document.data["emailVotosAprovar"] as? List<String>,
                                    document.data["emailVotosEliminar"] as? List<String>
                                )
                            )
                        //}
                    }
                    FirebaseViewModel._locations.value = localidades
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }

        fun getCategorias() {
            val db = Firebase.firestore

            db.collection("Categorias").get()
                .addOnSuccessListener { result ->
                    val categorias = mutableListOf<Triple<String, String, String>>()
                    for (document in result) {
                        if (document.data["estado"].toString() == "aprovado") {
                            categorias.add(
                                Triple(
                                    document.data["nome"].toString(),
                                    document.data["descrição"].toString(),
                                    document.data["imagemURL"].toString()
                                )
                            )
                        }
                    }
                    FirebaseViewModel._categorias.value = categorias
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }

        fun getLocaisInteresse() {
            val db = Firebase.firestore

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse")
                .get()
                .addOnSuccessListener { result ->
                    val locaisInteresse = mutableListOf<LocalInteresse>()

                    for (document in result) {
                        //if (document.data["estado"].toString() == "aprovado" || document.data["estado"].toString() == "pendente:apagar") {

                            var media = 0.0
                            var nClassificacoes = 0

                            db.collection("Localidades")
                                .document(FirebaseViewModel.currentLocation.value.toString())
                                .collection("Locais de Interesse")
                                .document(document.data["nome"].toString())
                                .collection("Classificação")
                                .get()
                                .addOnSuccessListener { resultados ->
                                    for (documento in resultados) {
                                        media += documento.data["valor"].toString().toDouble()
                                        ++nClassificacoes
                                    }

                                    media /= nClassificacoes

                                    locaisInteresse.add(
                                        LocalInteresse(
                                                document.data["nome"].toString(),
                                                document.data["descrição"].toString(),
                                                document.data["imagemURL"].toString(),
                                                document.data["categoria"].toString(),
                                                media.toString(),
                                                document.data["coordenadas"].toString(),
                                                document.data["email"].toString(),
                                                document.data["estado"].toString(),
                                                document.data["emailVotosAprovar"] as? List<String>,
                                                document.data["emailVotosEliminar"] as? List<String>
                                        ),
                                    )

                                    // Update UI or perform any other action here
                                    FirebaseViewModel._locaisInteresse.value = locaisInteresse
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("TAG", "Error getting classifications.", exception)
                                }
                        //}
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }


        fun addLocation(
            nome: String,
            descricao: String,
            imagePath: MutableState<String?>,
            owner_email: String
        ) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString())
            //val imgURL = getImageURL(imagePath.value.toString())

            val data = hashMapOf(
                "nome" to nome,
                "descrição" to descricao,
                "imagemURL" to "", //imgURL / imagePath.value.toString()
                "coordenadas" to GeoPoint(0.0, 0.0),
                "estado" to "pendente",
                "email" to owner_email
            )

            db.collection("Localidades").document(nome).set(data)
                .addOnSuccessListener { getLocations() }
                .addOnFailureListener { }

        }

        fun addLocalInteresse(
            nome: String,
            descricao: String,
            categoria: String,
            imagePath: MutableState<String?>,
            owner_email: String
        ) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString())
            //val imgURL = getImageURL(imagePath.value.toString())

            val data = hashMapOf(
                "nome" to nome,
                "descrição" to descricao,
                "categoria" to categoria,
                "classificação" to 0,
                "coordenadas" to GeoPoint(0.0, 0.0),
                "imagemURL" to "", //imgURL / imagePath.value.toString()
                "estado" to "pendente",
                "email" to owner_email
            )

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(nome).set(data)
                .addOnSuccessListener { getLocaisInteresse() }
                .addOnFailureListener { }
        }

        fun addCategoria(
            nome: String,
            descricao: String,
            imagePath: MutableState<String?>,
            owner_email: String
        ) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString())
            //val imgURL = getImageURL(imagePath.value.toString())

            val data = hashMapOf(
                "nome" to nome,
                "descrição" to descricao,
                "imagemURL" to "", //imgURL / imagePath.value.toString()
                "estado" to "pendente",
                "email" to owner_email
            )

            db.collection("Categorias").document(nome).set(data)
                .addOnSuccessListener { getCategorias() }
                .addOnFailureListener { }
        }


        fun updateLocation(
            nome: String,
            descricao: String,
            imagePath: MutableState<String?>,
            owner_email: String,
            oldName: String
        ) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString())
            //val imgURL = getImageURL(imagePath.value.toString())

            var data = hashMapOf(
                "nome" to nome,
                "descrição" to descricao,
                "imagemURL" to "", //imgURL / imagePath.value.toString()
                "estado" to "pendente",
                "email" to owner_email
            )


            db.collection("Localidades").document(nome).set(data)
                .addOnSuccessListener { getLocations() }
                .addOnFailureListener { }

            db.collection("Localidades").document(oldName).delete().addOnSuccessListener { getLocations() }
        }

        fun updateLocalInteresse(
            nome: String,
            descricao: String,
            categoria: String,
            imagePath: MutableState<String?>,
            owner_email: String,
            oldName: String
        ) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString())
            //val imgURL = getImageURL(imagePath.value.toString())

            var data = hashMapOf(
                "nome" to nome,
                "descrição" to descricao,
                "categoria" to categoria,
                "classificação" to 0,
                "coordenadas" to GeoPoint(0.0, 0.0),
                "imagemURL" to "", //imgURL / imagePath.value.toString()
                "estado" to "pendente",
                "email" to owner_email
            )


            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(nome).set(data)
                .addOnSuccessListener { getLocaisInteresse() }
                .addOnFailureListener { }

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(oldName).delete()
                .addOnSuccessListener { getLocaisInteresse() }
        }

        fun updateClassificacao(nome: String, addClassificacao: String, email: String) {
            val db = Firebase.firestore

            val data = hashMapOf(
                "valor" to addClassificacao.toInt(),
            )

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(nome).collection("Classificação")
                .document(email).set(data)
                .addOnSuccessListener {  } //.addOnSuccessListener { getLocaisInteresse() }
                .addOnFailureListener { }
        }

        fun deleteLocalInteresse(nome: String) {
            val db = Firebase.firestore

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(nome).collection("Classificação")
                .get()
                .addOnSuccessListener { resultados ->
                    if (!resultados.isEmpty) {

                        db.collection("Localidades")
                            .document(FirebaseViewModel.currentLocation.value.toString())
                            .collection("Locais de Interesse").document(nome)
                            .update("estado","pendente:apagar")

                            .addOnSuccessListener { getLocaisInteresse() }
                            .addOnFailureListener {}
                    } else {

                        db.collection("Localidades")
                            .document(FirebaseViewModel.currentLocation.value.toString())
                            .collection("Locais de Interesse").document(nome)
                            .delete()
                            .addOnSuccessListener { getLocaisInteresse() }
                            .addOnFailureListener {}
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure appropriately, e.g., log an error
                }
        }

        fun voteToDelete(nome: String, email: String) {
            val db = Firebase.firestore

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(nome)
                .get()
                .addOnSuccessListener { resultados ->

                    if (resultados.data?.get("emailVotosEliminar") != null) {

                        if((resultados.data!!.get("emailVotosEliminar") as List<Any?>).size == 2){
                            db.collection("Localidades")
                                .document(FirebaseViewModel.currentLocation.value.toString())
                                .collection("Locais de Interesse").document(nome)
                                .delete()
                                .addOnSuccessListener { getLocaisInteresse() }
                        }
                        else {

                            val votos =
                                resultados.data?.get("emailVotosEliminar") as MutableList<String>
                            votos.add(email)

                            db.collection("Localidades")
                                .document(FirebaseViewModel.currentLocation.value.toString())
                                .collection("Locais de Interesse").document(nome)
                                .update("emailVotosEliminar", votos)

                                .addOnSuccessListener { getLocaisInteresse() }
                                .addOnFailureListener {}
                        }
                    }
                    else {
                        val votos = mutableListOf<String>()
                        votos.add(email)

                        db.collection("Localidades")
                            .document(FirebaseViewModel.currentLocation.value.toString())
                            .collection("Locais de Interesse").document(nome)
                            .update("emailVotosEliminar", votos)

                            .addOnSuccessListener { getLocaisInteresse() }
                            .addOnFailureListener {}
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure appropriately, e.g., log an error
                }

        }

        fun voteToAproveLocation(nome: String, email: String) {
            val db = Firebase.firestore

            db.collection("Localidades")
                .document(nome)
                .get()
                .addOnSuccessListener { resultados ->

                    if (resultados.data?.get("emailVotosAprovar") != null) {
                        if((resultados.data!!.get("emailVotosAprovar") as List<Any?>).size == 1){

                            // APROVAR
                            db.collection("Localidades")
                                .document(nome)
                                .update("estado","aprovado")
                                .addOnSuccessListener { getLocations() }

                            // APAGAR LISTA DE EMAILS DE VOTOS
                            db.collection("Localidades")
                                .document(nome)
                                .update("emailVotosAprovar",null)
                        }
                        else {

                            val votos =
                                resultados.data?.get("emailVotosAprovar") as MutableList<String>
                            votos.add(email)

                            db.collection("Localidades")
                                .document(nome)
                                .update("emailVotosAprovar", votos)

                                .addOnSuccessListener { getLocaisInteresse() }
                                .addOnFailureListener {}
                        }
                    }
                    else {
                        val votos = mutableListOf<String>()
                        votos.add(email)

                        db.collection("Localidades")
                            .document(nome)
                            .update("emailVotosAprovar", votos)

                            .addOnSuccessListener {  }
                            .addOnFailureListener {}
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure appropriately, e.g., log an error
                }
        }

        fun voteToAprove(nome : String, email : String) {
            val db = Firebase.firestore

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(nome)
                .get()
                .addOnSuccessListener { resultados ->

                    if (resultados.data?.get("emailVotosAprovar") != null) {
                        if((resultados.data!!.get("emailVotosAprovar") as List<Any?>).size == 1){

                            // APROVAR
                            db.collection("Localidades")
                                .document(FirebaseViewModel.currentLocation.value.toString())
                                .collection("Locais de Interesse").document(nome)
                                .update("estado","aprovado")
                                .addOnSuccessListener { getLocaisInteresse() }

                            // APAGAR LISTA DE EMAILS DE VOTOS
                            db.collection("Localidades")
                                .document(FirebaseViewModel.currentLocation.value.toString())
                                .collection("Locais de Interesse").document(nome)
                                .update("emailVotosAprovar",null)
                        }
                        else {

                            val votos =
                                resultados.data?.get("emailVotosAprovar") as MutableList<String>
                            votos.add(email)

                            db.collection("Localidades")
                                .document(FirebaseViewModel.currentLocation.value.toString())
                                .collection("Locais de Interesse").document(nome)
                                .update("emailVotosAprovar", votos)

                                .addOnSuccessListener { getLocaisInteresse() }
                                .addOnFailureListener {}
                        }
                    }
                    else {
                        val votos = mutableListOf<String>()
                        votos.add(email)

                        db.collection("Localidades")
                            .document(FirebaseViewModel.currentLocation.value.toString())
                            .collection("Locais de Interesse").document(nome)
                            .update("emailVotosAprovar", votos)

                            .addOnSuccessListener {  }
                            .addOnFailureListener {}
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure appropriately, e.g., log an error
                }
        }

    }
}