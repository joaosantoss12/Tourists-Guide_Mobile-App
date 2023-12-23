package pt.isec.a2020136093.amov.guiaturistico.utils

import android.content.res.AssetManager
import android.icu.util.Calendar
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import pt.isec.a2020136093.amov.guiaturistico.viewModel.Categoria
import pt.isec.a2020136093.amov.guiaturistico.viewModel.Comentario
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocalInteresse
import pt.isec.a2020136093.amov.guiaturistico.viewModel.Localizacao
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocationViewModel
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.CompletableFuture


class FirebaseStorageUtil {

    companion object {

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

        fun uploadFile(imgPath: String) : CompletableFuture<String> {
            val storage = Firebase.storage
            val storageReference = storage.reference

            val file = File(imgPath)
            val newFileNameInsideStorage = storageReference.child(file.name)

            val future = CompletableFuture<String>()


            val uploadTask = newFileNameInsideStorage.putFile(Uri.fromFile(file))
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        future.completeExceptionally(it)
                    }
                }
                newFileNameInsideStorage.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    future.complete(downloadUri.toString())
                }
            }
            return future
        }



        fun getLocations() {
            val db = Firebase.firestore

            db.collection("Localidades").get()
                .addOnSuccessListener { result ->
                    val localidades = mutableListOf<Localizacao>()
                    for (document in result) {
                        localidades.add(
                            Localizacao(
                                document.data["nome"].toString(),
                                document.data["descrição"].toString(),
                                document.data["imagemURL"].toString(),
                                document.data["coordenadas"] as? GeoPoint,
                                document.data["email"].toString(),
                                document.data["estado"].toString(),
                                document.data["emailVotosAprovar"] as? List<String>,
                                document.data["emailVotosEliminar"] as? List<String>,
                                0.0
                            )
                        )
                    }
                    calculateDistancesLocalizacao(localidades)
                    FirebaseViewModel._locations.value = localidades
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }
        private fun calculateDistancesLocalizacao(localidades: List<Localizacao>) {
            localidades.forEach {
                it.distance = Location("").apply {
                    latitude = it.coordenadas?.latitude ?: 0.0
                    longitude = it.coordenadas?.longitude ?: 0.0
                }.distanceTo(Location("").apply {
                    latitude = LocationViewModel.currentLocation.value?.latitude ?: 0.0
                    longitude = LocationViewModel.currentLocation.value?.longitude ?: 0.0
                }).toDouble() / 1000
            }
        }

        fun getCategorias() {
            val db = Firebase.firestore

            db.collection("Categorias").get()
                .addOnSuccessListener { result ->
                    val categorias = mutableListOf<Categoria>()
                    for (document in result) {
                        categorias.add(
                            Categoria(
                                document.data["nome"].toString(),
                                document.data["descrição"].toString(),
                                document.data["imagemURL"].toString(),
                                document.data["email"].toString(),
                                document.data["estado"].toString(),
                                document.data["emailVotosAprovar"] as? List<String>
                            )
                        )
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
                                                document.data["coordenadas"] as? GeoPoint,
                                                document.data["email"].toString(),
                                                document.data["estado"].toString(),
                                                document.data["emailVotosAprovar"] as? List<String>,
                                                document.data["emailVotosEliminar"] as? List<String>,
                                            0.0
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


        fun addLocation(nome: String, descricao: String, imagePath: MutableState<String?>, owner_email: String) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString()).thenAccept{downloadUri ->
                val data = hashMapOf(
                    "nome" to nome,
                    "descrição" to descricao,
                    "imagemURL" to downloadUri, //imgURL / imagePath.value.toString()
                    "coordenadas" to GeoPoint(0.0, 0.0),
                    "estado" to "pendente",
                    "email" to owner_email
                )

                db.collection("Localidades").document(nome).set(data)
                    .addOnSuccessListener { getLocations() }
                    .addOnFailureListener { }
            }

        }

        fun addLocalInteresse(nome: String, descricao: String, categoria: String, imagePath: MutableState<String?>, owner_email: String) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString()).thenAccept{ downloadUri ->
                val data = hashMapOf(
                    "nome" to nome,
                    "descrição" to descricao,
                    "categoria" to categoria,
                    "classificação" to 0,
                    "coordenadas" to GeoPoint(0.0, 0.0),
                    "imagemURL" to downloadUri, //imgURL / imagePath.value.toString()
                    "estado" to "pendente",
                    "email" to owner_email
                )

                db.collection("Localidades")
                    .document(FirebaseViewModel.currentLocation.value.toString())
                    .collection("Locais de Interesse").document(nome).set(data)
                    .addOnSuccessListener { getLocaisInteresse() }
                    .addOnFailureListener { }
            }
        }

        fun addCategoria(nome: String, descricao: String, imagePath: MutableState<String?>, owner_email: String) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString()).thenAccept{downloadUri ->
                val data = hashMapOf(
                    "nome" to nome,
                    "descrição" to descricao,
                    "imagemURL" to downloadUri, //imgURL / imagePath.value.toString()
                    "estado" to "pendente",
                    "email" to owner_email
                )

                db.collection("Categorias").document(nome).set(data)
                    .addOnSuccessListener { getCategorias() }
                    .addOnFailureListener { }

            }



        }


        fun updateLocation(nome: String, descricao: String, imagePath: MutableState<String?>, owner_email: String, oldName: String) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString()).thenAccept { downloadUri ->
                val data = hashMapOf(
                    "nome" to nome,
                    "descrição" to descricao,
                    "imagemURL" to downloadUri, //imgURL / imagePath.value.toString()
                    "estado" to "pendente",
                    "email" to owner_email
                )


                db.collection("Localidades").document(nome).set(data)
                    .addOnSuccessListener { getLocations() }
                    .addOnFailureListener { }

                db.collection("Localidades").document(oldName).delete().addOnSuccessListener { getLocations() }
            }
        }

        fun updateLocalInteresse(nome: String, descricao: String, categoria: String, imagePath: MutableState<String?>, owner_email: String, oldName: String) {
            val db = Firebase.firestore

            uploadFile(imagePath.value.toString()).thenAccept { downloadUri ->
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

                db.collection("Localidades")
                    .document(FirebaseViewModel.currentLocation.value.toString())
                    .collection("Locais de Interesse").document(oldName).delete()
                    .addOnSuccessListener { getLocaisInteresse() }
            }

        }




        fun updateClassificacao(nome: String, addClassificacao: String, email: String) {
            val db = Firebase.firestore

            val data = hashMapOf(
                "valor" to addClassificacao.toInt()
            )

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(nome).collection("Classificação")
                .document(email).set(data)
                .addOnSuccessListener {  } //.addOnSuccessListener { getLocaisInteresse() }
                .addOnFailureListener { }
        }

        fun deleteLocalizacao(nome: String) {
            val db = Firebase.firestore

            db.collection("Localidades")
                .document(nome)
                .collection("Locais de Interesse")
                .get()
                .addOnSuccessListener { resultados ->
                    if (resultados.isEmpty) {

                        db.collection("Localidades")
                            .document(nome)
                            .delete()

                            .addOnSuccessListener { getLocations() }
                            .addOnFailureListener {}
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure appropriately, e.g., log an error
                }

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

        fun voteToAproveCategories(nome: String, email: String) {
            val db = Firebase.firestore

            db.collection("Categorias")
                .document(nome)
                .get()
                .addOnSuccessListener { resultados ->

                    if (resultados.data?.get("emailVotosAprovar") != null) {
                        if((resultados.data!!.get("emailVotosAprovar") as List<Any?>).size == 1){

                            // APROVAR
                            db.collection("Categorias")
                                .document(nome)
                                .update("estado","aprovado")
                                .addOnSuccessListener { getCategorias() }

                            // APAGAR LISTA DE EMAILS DE VOTOS
                            db.collection("Categorias")
                                .document(nome)
                                .update("emailVotosAprovar",null)
                        }
                        else {
                            val votos = resultados.data?.get("emailVotosAprovar") as MutableList<String>
                            votos.add(email)

                            db.collection("Categorias")
                                .document(nome)
                                .update("emailVotosAprovar", votos)

                                .addOnSuccessListener { getCategorias() }
                                .addOnFailureListener {}
                        }
                    }
                    else {
                        val votos = mutableListOf<String>()
                        votos.add(email)

                        db.collection("Categorias")
                            .document(nome)
                            .update("emailVotosAprovar", votos)

                            .addOnSuccessListener { getCategorias() }
                            .addOnFailureListener {}
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure appropriately, e.g., log an error
                }
        }

        fun deleteCategoria(nome: String) {
            val db = Firebase.firestore
            var count = 0
            var totalDocuments = 0

            db.collection("Localidades")
                .get()
                .addOnSuccessListener { resultados ->

                    totalDocuments = resultados.size()

                    for (document in resultados) {
                        db.collection("Localidades")
                            .document(document.data["nome"].toString())
                            .collection("Locais de Interesse")
                            .get()
                            .addOnSuccessListener { resultados2 ->

                                for (document2 in resultados2) {
                                    if (document2.data["categoria"].toString() == nome) {
                                        Log.i("-", "-")
                                        return@addOnSuccessListener
                                    }
                                }

                                count++
                                if (count == totalDocuments) {
                                    Log.i("-", "-")
                                    db.collection("Categorias")
                                        .document(nome)
                                        .delete()

                                        .addOnSuccessListener { getCategorias() }
                                }
                            }
                    }
                }

        }



        fun getComentarios() {
            val db = Firebase.firestore

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(FirebaseViewModel.currentLocalInteresse.value.toString())
                .collection("Comentários")
                .get()
                .addOnSuccessListener { result ->
                    val comentarios = mutableListOf<Comentario>()

                    for (document in result) {
                        comentarios.add(
                            Comentario(
                                document.data["ano"].toString(),
                                document.data["mês"].toString(),
                                document.data["dia"].toString(),
                                document.data["texto"].toString(),
                                document.data["email"].toString()
                            )
                        )
                    }
                    FirebaseViewModel._comentarios.value = comentarios
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }

        }

        fun addComentario(texto: String, email: String) {
            val db = Firebase.firestore

            var nextCommentID = 0

            db.collection("Localidades")
                .document(FirebaseViewModel.currentLocation.value.toString())
                .collection("Locais de Interesse").document(FirebaseViewModel.currentLocalInteresse.value.toString())
                .collection("Comentários").get()
                .addOnSuccessListener {
                    for (document in it) {
                        nextCommentID = document.id.toInt() + 1
                    }

                    val data = hashMapOf(
                        "ano" to Calendar.getInstance().get(Calendar.YEAR).toString(),
                        "mês" to (Calendar.getInstance().get(Calendar.MONTH) + 1).toString(),
                        "dia" to Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString(),
                        "texto" to texto,
                        "email" to email
                    )

                    db.collection("Localidades")
                        .document(FirebaseViewModel.currentLocation.value.toString())
                        .collection("Locais de Interesse").document(FirebaseViewModel.currentLocalInteresse.value.toString())
                        .collection("Comentários").document(nextCommentID.toString()).set(data)
                        .addOnSuccessListener { getComentarios() }
                        .addOnFailureListener { }

                    getComentarios()
                }
        }

    }
}