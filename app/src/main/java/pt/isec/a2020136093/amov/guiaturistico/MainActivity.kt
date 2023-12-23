package pt.isec.a2020136093.amov.guiaturistico

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.TrabalhoPráticoGuiaTuristicoTheme
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocationViewModel
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocationViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModelFirebase: FirebaseViewModel by viewModels()
    private val app by lazy { application as LocationMapsApp }
    private val viewModelLocation: LocationViewModel by viewModels {
        LocationViewModelFactory(app.locationHandler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            this,
            PreferenceManager.getDefaultSharedPreferences(this)
        )

        setContent {
            TrabalhoPráticoGuiaTuristicoTheme {
                MainScreen(viewModelFirebase, viewModelLocation)
            }
        }

        verifyPermissions()

        if (
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            verifyMultiplePermissions.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            verifySinglePermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    val verifyMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        //if (it.values.contains(false))
        //finish()
    }

    val verifySinglePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        //if (!it)
        //finish()
    }

    override fun onResume() {
        super.onResume()
        viewModelLocation.startLocationUpdates()
    }

    private fun verifyPermissions(): Boolean {
        viewModelLocation.coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        viewModelLocation.fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModelLocation.backgroundLocationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else
            viewModelLocation.backgroundLocationPermission =
                viewModelLocation.coarseLocationPermission || viewModelLocation.fineLocationPermission

        if (!viewModelLocation.coarseLocationPermission && !viewModelLocation.fineLocationPermission) {
            basicPermissionsAuthorization.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return false
        } else
            verifyBackgroundPermission()
        return true
    }

    private val basicPermissionsAuthorization = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        viewModelLocation.coarseLocationPermission =
            results[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        viewModelLocation.fineLocationPermission =
            results[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        viewModelLocation.startLocationUpdates()
        verifyBackgroundPermission()
    }

    private fun verifyBackgroundPermission() {
        if (!(viewModelLocation.coarseLocationPermission || viewModelLocation.fineLocationPermission))
            return

        if (!viewModelLocation.backgroundLocationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                val dlg = AlertDialog.Builder(this)
                    .setTitle("Background Location")
                    .setMessage(
                        "This application needs your permission to use location while in the background.\n" +
                                "Please choose the correct option in the following screen" +
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                    " (\"${packageManager.backgroundPermissionOptionLabel}\")."
                                else
                                    "."
                    )
                    .setPositiveButton("Ok") { _, _ ->
                        backgroundPermissionAuthorization.launch(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    }
                    .create()
                dlg.show()
            }
        }
    }

    private val backgroundPermissionAuthorization = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        viewModelLocation.backgroundLocationPermission = result
        Toast.makeText(this, "Background location enabled: $result", Toast.LENGTH_LONG).show()
    }
}