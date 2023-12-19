package pt.isec.a2020136093.amov.guiaturistico

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.TrabalhoPráticoGuiaTuristicoTheme
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

class MainActivity : ComponentActivity() {
    val viewModel : FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrabalhoPráticoGuiaTuristicoTheme {
                MainScreen(viewModel)
            }
        }
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
            ) != PackageManager.PERMISSION_GRANTED) {
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

}