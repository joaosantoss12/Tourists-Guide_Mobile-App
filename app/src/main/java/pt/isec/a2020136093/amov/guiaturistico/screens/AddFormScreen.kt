package pt.isec.a2020136093.amov.guiaturistico.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.a2020136093.amov.guiaturistico.ui.composables.SelectImage
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.RegularFont
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

@Composable
fun AddFormScreen(
    viewModel : FirebaseViewModel,
    navController: NavController,
){

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var coordanadas by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Adicionar ${viewModel.tipoAddForm.value}",   // Localização, Categoria, Local de Interesse
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = RegularFont,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
            },
            label = { Text(text = "Nome") },
            modifier = Modifier
                .fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descricao,
            onValueChange = {
                descricao = it
            },
            label = { Text(text = "Descrição") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(viewModel.tipoAddForm.value == "Local de Interesse"){
            OutlinedTextField(
                value = categoria,
                onValueChange = {
                    categoria = it
                },
                label = { Text(text = "Categoria") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = coordanadas,
                onValueChange = {
                    coordanadas = it
                },
                label = { Text(text = "Coordenadas") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        //cria um quadrado para o preview da imagem de galeria ou camera
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            SelectImage(viewModel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(onClick = {
                when(viewModel.tipoAddForm.value){
                    "Localização" -> {
                        viewModel.addLocation_firebase(nome,descricao)
                        navController.navigate("Home")
                    }
                    "Categoria" -> {}
                    "Local de Interesse" -> {
                        viewModel.addLocalInteresse_firebase(nome,descricao,categoria)
                        navController.navigate("Interests")
                    }
                }
            }) {
                Text(text = "Confirmar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(onClick = {
                when(viewModel.tipoAddForm.value){
                    "Localização" -> navController.navigate("Home")
                    "Categoria" -> navController.navigate("Interests")
                    "Local de Interesse" -> navController.navigate("Interests")
                }
            }) {
                Text(text = "Cancelar")
            }
        }
    }
}