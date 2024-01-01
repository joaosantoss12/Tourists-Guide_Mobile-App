package pt.isec.a2020136093.amov.guiaturistico.viewModel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.a2020136093.amov.guiaturistico.utils.location.LocationHandler

class LocationViewModelFactory(private val locationHandler: LocationHandler)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(locationHandler) as T
    }
}

data class Coordinates(val team: String,val latitude : Double, val longitude: Double, val metodo : String)

class LocationViewModel(private val locationHandler: LocationHandler) : ViewModel() {

    val listaLocaisInteresse= FirebaseViewModel.locaisInteresse

    val listaLocalizacoes= FirebaseViewModel.locations

    val POIs_locaisInteresse
        get() = listaLocaisInteresse.value
            ?.filter { it.estado == "aprovado" }
            ?.map {
                Coordinates(it.nome, it.coordenadas?.latitude ?: 0.0, it.coordenadas?.longitude ?: 0.0, it.metodo)
            }
            ?: emptyList()


    val POIs_localizacoes
        get() = listaLocalizacoes.value
            ?.filter { it.estado == "aprovado" }
            ?.map {
                Coordinates(it.nome, it.coordenadas?.latitude ?: 0.0, it.coordenadas?.longitude ?: 0.0, it.metodo)
            }
            ?: emptyList()

    // Permissions
    var coarseLocationPermission = false
    var fineLocationPermission = false
    var backgroundLocationPermission = false


    companion object {
        val _showLocations = MutableLiveData(false)
        val showLocations: LiveData<Boolean>
            get() = _showLocations


        private val _currentLocation = MutableLiveData(Location(null))
        val currentLocation: LiveData<Location>
            get() = _currentLocation
    }



    init {
        locationHandler.onLocation = {location ->
            _currentLocation.value = location
        }
    }

    fun startLocationUpdates() {
        if (fineLocationPermission && coarseLocationPermission) {
            locationHandler.startLocationUpdates()
        }
    }

    fun stopLocationUpdates() {
        locationHandler.stopLocationUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}