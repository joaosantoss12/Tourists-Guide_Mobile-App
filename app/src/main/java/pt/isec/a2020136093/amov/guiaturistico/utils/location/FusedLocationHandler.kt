package pt.isec.a2020136093.amov.guiaturistico.utils.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority.*

class FusedLocationHandler(private val locationProvider: FusedLocationProviderClient) : LocationHandler {
    override var locationEnabled = false
    override var onLocation: ((Location) -> Unit)? = null

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        if (locationEnabled)
            return

        val notify = onLocation ?: return

        notify(Location(null).apply {
            latitude = 40.1925
            longitude = -8.4128
        })

        locationProvider.lastLocation
            .addOnSuccessListener { location ->
                location?.let(notify)
                //Log.i("Teste", "startLocationUpdates: $location")
            }
        val locationRequest =
            LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, 2000) //.setMinUpdateDistanceMeters(100f) //.setMinUpdateIntervalMillis(1000) //.setMaxUpdateDelayMillis(10000) //.setPriority(PRIORITY_HIGH_ACCURACY) //.setIntervalMillis(1000) //.setMaxUpdates(10)
                .build()

        locationProvider.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

        locationEnabled = true
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.forEach(onLocation)
        }
    }
    override fun stopLocationUpdates() {
        if (!locationEnabled)
            return
        locationProvider.removeLocationUpdates(locationCallback)
        locationEnabled = false
    }

}