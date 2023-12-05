package pt.isec.a2020136093.amov.guiaturistico

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
import pt.isec.a2020136093.amov.MainScreen
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.TrabalhoPráticoGuiaTuristicoTheme
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

class MainActivity : ComponentActivity() {
    val viewModel : FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrabalhoPráticoGuiaTuristicoTheme {
                // A surface container using the 'background' color from the theme
                /*Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(getString(R.string.app_name))
                }*/
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrabalhoPráticoGuiaTuristicoTheme {
        Greeting("Android")
    }
}