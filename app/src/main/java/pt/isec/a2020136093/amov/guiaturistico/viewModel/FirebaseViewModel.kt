package pt.isec.a2020136093.amov.guiaturistico.viewModel

import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pt.isec.a2020136093.amov.guiaturistico.utils.FirebaseAuthUtil
import pt.isec.a2020136093.amov.guiaturistico.utils.FirebaseStorageUtil
import kotlinx.coroutines.tasks.await

//import pt.isec.a2020136093.amov.guiaturistico.utils.FStorageUtil


class Localizacao(
    val nome : String,
    val descricao : String,
    val imagemURL : String,
    val coordenadas : GeoPoint?,
    val email : String,
    val estado : String,
    val emailVotosAprovar : List<String>?,
    val emailVotosEliminar : List<String>?,
    var distance: Double?,
    var metodo : String
)

class LocalInteresse(
    val nome : String,
    val descricao : String,
    val imagemURL : String,
    val categoria : String,
    val classificacao : String,
    val coordenadas : GeoPoint?,
    val email : String,
    val estado : String,
    val emailVotosAprovar : List<String>?,
    val emailVotosEliminar : List<String>?,
    var distance: Double?,
    var metodo : String
)

class Categoria(
    val nome : String,
    val descricao : String,
    val imagemURL : String,
    val email : String,
    val estado : String,
    val emailVotosAprovar : List<String>?,
)

class Comentario(
    val ano : String,
    val mes : String,
    val dia : String,
    val texto : String,
    val email : String,
)


data class User(val name : String, val email : String, val picture : String?)

fun FirebaseUser.toUser() : User {
    val username = this.displayName ?: ""
    val str_email = this.email ?: ""
    val photoUrl = this.photoUrl.toString()

    return User(username, str_email, photoUrl)
}

class FirebaseViewModel : ViewModel() {
    val imagePath : MutableState<String?> = mutableStateOf(null)
    val tipoAddForm : MutableState<String?> = mutableStateOf(null)
    val tipoEditForm : MutableState<String?> = mutableStateOf(null)
    var editName = ""
    var selectedCategory = ""

    companion object {
        val _locations = MutableLiveData<MutableList<Localizacao>>()
        val locations: LiveData<MutableList<Localizacao>>
            get() = _locations

        val _categorias = MutableLiveData<MutableList<Categoria>>()
        val categorias: LiveData<MutableList<Categoria>>
            get() = _categorias

        val _locaisInteresse = MutableLiveData<MutableList<LocalInteresse>>()
        val locaisInteresse: LiveData<MutableList<LocalInteresse>>
            get() = _locaisInteresse

        val _comentarios = MutableLiveData<MutableList<Comentario>>()
        val comentarios: LiveData<MutableList<Comentario>>
            get() = _comentarios



        val _currentLocation = MutableLiveData<String>()
        val currentLocation: LiveData<String>
            get() = _currentLocation

        val _currentLocalInteresse = MutableLiveData<String>()
        val currentLocalInteresse: LiveData<String>
            get() = _currentLocalInteresse
    }

    private val _user = mutableStateOf(FirebaseAuthUtil.currentUser?.toUser())
    val user : MutableState<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error : MutableState<String?>
        get() = _error

    fun createUserWithEmail(email: String, password: String) {
        if(email.isBlank() || password.isBlank()) {
            _error.value = "O email ou password não podem estar vazios"
            return
        }

        viewModelScope.launch {
            FirebaseAuthUtil.createUserWithEmail(email, password) { execption ->
                if (execption == null) {
                    _user.value = FirebaseAuthUtil.currentUser?.toUser()
                    _error.value = null;
                }
                else {
                    _error.value = execption.message
                }
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if(email.isBlank() || password.isBlank())
            return

        viewModelScope.launch {
            FirebaseAuthUtil.signInWithEmail(email, password) { execption ->
                if (execption == null) {
                    // SUCESSO
                    _user.value = FirebaseAuthUtil.currentUser?.toUser()
                } else {
                    // ERRO
                    _error.value = execption.message
                }
            }
        }
    }

    fun signOut(){
        FirebaseAuthUtil.signOut()
        _user.value = null
    }

    fun getLocations(){
        viewModelScope.launch {
            FirebaseStorageUtil.getLocations()
        }
    }
    fun getCategorias(){
        viewModelScope.launch {
            FirebaseStorageUtil.getCategorias()
        }
    }
    fun getLocaisInteresse(){
        viewModelScope.launch {
            FirebaseStorageUtil.getLocaisInteresse()
        }
    }

    fun addLocation_firebase(nome : String, descricao : String, latitude : String, longitude : String, metodo : String) {
        viewModelScope.launch{
            FirebaseStorageUtil.addLocation(nome,descricao,imagePath,user.value?.email!!,latitude,longitude,metodo)
        }
    }
    fun addLocalInteresse_firebase(nome : String, descricao : String, categoria : String, latitude : String, longitude : String, metodo : String) {
        viewModelScope.launch{
            FirebaseStorageUtil.addLocalInteresse(nome,descricao,categoria,imagePath,user.value?.email!!,latitude,longitude,metodo)
        }
    }
    fun addCategoria_firebase(nome : String, descricao : String) {
        viewModelScope.launch{
            FirebaseStorageUtil.addCategoria(nome,descricao,imagePath,user.value?.email!!)
        }
    }

    fun updateLocation_firebase(nome : String, descricao : String, latitude : String, longitude : String, metodo : String) {
        viewModelScope.launch{
            FirebaseStorageUtil.updateLocation(nome,descricao,imagePath,user.value?.email!!,editName,latitude,longitude,metodo)
        }
    }
    fun updateLocalInteresse_firebase(nome : String, descricao : String, categoria : String, latitude : String, longitude : String, metodo : String) {
        viewModelScope.launch{
            FirebaseStorageUtil.updateLocalInteresse(nome,descricao,categoria,imagePath,user.value?.email!!,editName,latitude,longitude,metodo)
        }
    }

    fun updateClassificacao_firebase(nome: String, addClassificacao: String) {
        viewModelScope.launch{
            FirebaseStorageUtil.updateClassificacao(nome,addClassificacao,user.value?.email!!)
        }
    }

    fun deleteLocalInteresse(nome: String) {
        viewModelScope.launch{
            FirebaseStorageUtil.deleteLocalInteresse(nome)
        }
    }

    fun voteToDelete(nome: String) {
        viewModelScope.launch{
            FirebaseStorageUtil.voteToDelete(nome,user.value?.email!!)
        }
    }

    fun voteToAproveLocation(nome: String) {
        viewModelScope.launch{
            FirebaseStorageUtil.voteToAproveLocation(nome,user.value?.email!!)
        }
    }

    fun voteToAprove(nome : String) {
        viewModelScope.launch{
            FirebaseStorageUtil.voteToAprove(nome,user.value?.email!!)
        }
    }

    fun deleteLocalizacao(nome: String) {
        viewModelScope.launch{
            FirebaseStorageUtil.deleteLocalizacao(nome)
        }
    }

    fun voteToAproveCategories(nome: String) {
        viewModelScope.launch{
            FirebaseStorageUtil.voteToAproveCategories(nome,user.value?.email!!)
        }
    }

    fun deleteCategoria(nome: String) {
        viewModelScope.launch{
            FirebaseStorageUtil.deleteCategoria(nome)
        }
    }



    fun getComentarios() {
        FirebaseStorageUtil.getComentarios()
    }

    fun addComentario(texto : String) {
        viewModelScope.launch{
            FirebaseStorageUtil.addComentario(texto, user.value?.email!!)
        }
    }


}