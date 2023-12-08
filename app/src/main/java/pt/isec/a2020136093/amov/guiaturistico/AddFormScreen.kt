package pt.isec.a2020136093.amov.guiaturistico

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.a2020136093.amov.guiaturistico.ui.composables.SelectGalleryImage
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.RegularFont
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

@Composable
fun AddFormScreen(
    viewModel : FirebaseViewModel,
    navController: NavController,
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Adicionar ${viewModel.tipoAddForm.value}",   // Localização, Categoria, Local de Interesse
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = RegularFont,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Nome") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Descrição") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //cria um quadrado para o preview da imagem de galeria ou camera
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            SelectGalleryImage(viewModel.imagePath)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            OutlinedButton(onClick = {
                when(viewModel.tipoAddForm.value){
                    "Localização" -> {}
                    "Categoria" -> {}
                    "Local_Interesse" -> {}
                }
            }) {
                Text(text = "Confirmar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(onClick = {
                when(viewModel.tipoAddForm.value){
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