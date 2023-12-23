package pt.isec.a2020136093.amov.guiaturistico.viewModel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.a2020136093.amov.guiaturistico.utils.location.LocationHandler
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel.Companion._currentLocation

class LocationViewModelFactory(private val locationHandler: LocationHandler)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(locationHandler) as T
    }
}
data class Coordinates(val team: String,val latitude : Double, val longitude: Double)

class LocationViewModel(private val locationHandler: LocationHandler) : ViewModel() {
    /*val POIs = listOf(
        Coordinates("Liverpool",53.430819,-2.960828),
        Coordinates("Manchester City",53.482989,-2.200292),
        Coordinates("Manchester United",53.463056,-2.291389),
        Coordinates("Bayern Munich", 48.218775, 11.624753),
        Coordinates("Barcelona",41.38087,2.122802),
        Coordinates("Real Madrid",40.45306,-3.68835),
        Coordinates("Juventus",45.109248,7.641602),
        Coordinates("Paris Saint-Germain",48.841389,2.253056),
        Coordinates("Chelsea",51.481667,-0.191111),
        Coordinates("Arsenal",51.555,-0.108611),
        Coordinates("Tottenham Hotspur",51.603333,-0.065833),
    )*/




    val listaLocaisInteresse= FirebaseViewModel.locaisInteresse

    val POIs
        get() = listaLocaisInteresse.value
            ?.filter { it.estado == "aprovado" } // Adiciona o filtro aqui
            ?.map {
                Coordinates(it.nome, it.coordenadas?.latitude ?: 0.0, it.coordenadas?.longitude ?: 0.0)
            }
            ?: emptyList()





    // Permissions
    var coarseLocationPermission = false
    var fineLocationPermission = false
    var backgroundLocationPermission = false






    companion object {
        private val _currentLocation = MutableLiveData(Location(null))
        val currentLocation: LiveData<Location>
            get() = _currentLocation
    }



    private val locationEnabled : Boolean
        get() = locationHandler.locationEnabled

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