package pt.isec.a2020136093.amov.guiaturistico

import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.RegularFont
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

@Composable
fun AddForm(
    navController: NavController,
    tipo : String,
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Adicionar $tipo",   // Localização, Categoria, Local de Interesse
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = RegularFont,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        when(tipo){
            "Localização" -> {}
            "Categoria" -> {}
            "Local_Interesse" -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            OutlinedButton(onClick = {
                when(tipo){
                    "Localização" -> {}
                    "Categoria" -> {}
                    "Local_Interesse" -> {}
                }
            }) {
                Text(text = "Confirmar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(onClick = {
                when(tipo){
                    "Localização" -> navController.navigate("HomeScreen")
                    "Categoria" -> navController.navigate("InterestsScreen")
                    "Local_Interesse" -> navController.navigate("InterestsScreen")
                }
            }) {
                Text(text = "Cancelar")
            }
        }
    }
}