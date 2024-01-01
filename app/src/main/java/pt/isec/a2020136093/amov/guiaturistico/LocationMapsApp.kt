package pt.isec.a2020136093.amov.guiaturistico

import android.app.Application
import com.google.android.gms.location.LocationServices
import pt.isec.a2020136093.amov.guiaturistico.utils.location.FusedLocationHandler
import pt.isec.a2020136093.amov.guiaturistico.utils.location.LocationHandler


class LocationMapsApp : Application() {
    val locationHandler : LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

    override fun onCreate() {
        super.onCreate()
    }
}