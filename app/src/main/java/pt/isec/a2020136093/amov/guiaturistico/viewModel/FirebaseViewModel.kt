package pt.isec.a2020136093.amov.guiaturistico.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import pt.isec.a2020136093.amov.guiaturistico.utils.FirebaseAuthUtil
//import pt.isec.a2020136093.amov.guiaturistico.utils.FStorageUtil

data class User(val name : String, val email : String, val picture : String?)

fun FirebaseUser.toUser() : User {
    val username = this.displayName ?: ""
    val str_email = this.email ?: ""
    val photoUrl = this.photoUrl.toString()

    return User(username, str_email, photoUrl)
}

class FirebaseViewModel : ViewModel() {
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
    /*
    fun addDataToFirestore(){
        viewModelScope.launch{
            FStorageUtil.addDataToFirestore{ exception ->
                _error.value = exception?.message
            }
        }
    }

    fun updateDataToFirestore() {
        viewModelScope.launch{
            FStorageUtil.updateDataInFirestore{ exception ->
                _error.value = exception?.message
            }
        }
    }

    fun startObserver(){
        viewModelScope.launch{
            FStorageUtil.startObserver(){games, topscore ->
                Log.i("TESTE", "******** $games $topscore ********")
            }
        }
    }*/
}