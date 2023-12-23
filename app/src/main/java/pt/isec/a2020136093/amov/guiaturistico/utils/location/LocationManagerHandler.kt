package pt.isec.a2020136093.amov.guiaturistico.utils.location

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import androidx.core.location.LocationListenerCompat

class LocationManagerHandler(private val locationManager: LocationManager) : LocationHandler {
    override var locationEnabled = false
    override var onLocation: ((Location) -> Unit)? = null

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        if (locationEnabled)
            return

        val notify = onLocation ?: return // if(onlocation == null) return;

        notify(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            ?: Location(null).apply {
                latitude = 40.1925
                longitude = -8.4128

            }
        )

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            100f,
            locationListener
        )
        locationEnabled = true
    }

    private val locationListener = LocationListenerCompat { location ->
        val notify = onLocation ?: return@LocationListenerCompat
        notify(location)
    }
    override fun stopLocationUpdates() {
        if (!locationEnabled)
            return
        locationManager.removeUpdates(locationListener)
        locationEnabled = false
    }

}