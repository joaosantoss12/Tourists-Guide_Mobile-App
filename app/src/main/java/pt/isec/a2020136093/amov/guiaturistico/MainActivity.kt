package pt.isec.a2020136093.amov.guiaturistico

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pt.isec.a2020136093.amov.MainScreen
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
        //verifyPermissions()
    }

}