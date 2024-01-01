package pt.isec.a2020136093.amov.guiaturistico.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.a2020136093.amov.guiaturistico.ui.composables.SelectImage
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.RegularFont
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocationViewModel

@Composable
fun EditFormScreen(
    viewModel : FirebaseViewModel,
    navController: NavController,
){

    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    var metodo by remember { mutableStateOf("utilizador") }

    val contexto = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Editar ${viewModel.tipoEditForm.value}",   // Localização, Categoria, Local de Interesse
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
            value = descricao,
            onValueChange = {
                descricao = it
            },
            label = { Text(text = "Descrição") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(viewModel.tipoEditForm.value == "Local de Interesse"){
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

        }

        if(viewModel.tipoEditForm.value != "Categoria") {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = {
                        latitude = it
                    },
                    label = { Text(text = "Latitude") },
                    modifier = Modifier
                        .weight(1f)

                )

                Spacer(modifier = Modifier.width(5.dp))

                OutlinedTextField(
                    value = longitude,
                    onValueChange = {
                        longitude = it
                    },
                    label = { Text(text = "Longitude") },
                    modifier = Modifier
                        .weight(1f)

                )

                Spacer(modifier = Modifier.width(5.dp))

                OutlinedButton(
                    onClick = {
                        latitude = LocationViewModel.currentLocation.value?.latitude.toString()
                        longitude = LocationViewModel.currentLocation.value?.longitude.toString()

                        metodo = "automático"
                    },
                    shape = CircleShape, // Forma circular
                ) {
                    Icon(
                        Icons.Filled.LocationOn, "localização atual",
                        tint = Color.Black,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

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
                when(viewModel.tipoEditForm.value){
                    "Localização" -> {
                        if(descricao=="" || latitude=="" || longitude=="" || viewModel.imagePath.value==null) {
                            Toast.makeText(
                                contexto,
                                "Preencha todos os campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else if(latitude.toDouble() < -90 || latitude.toDouble() > 90 || longitude.toDouble() < -180 || longitude.toDouble() > 180){
                            Toast.makeText(
                                contexto,
                                "Valores de coordenadas inválidas",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else{
                            Toast.makeText(
                                contexto,
                                "Localização editada com sucesso",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.updateLocation_firebase(descricao,latitude,longitude,metodo)
                            navController.navigate("Home")
                        }
                    }
                    "Categoria" -> {
                        viewModel.updateCategoria_firebase(descricao)
                        navController.navigate("Interests")
                    }
                    "Local de Interesse" -> {
                        if(descricao=="" || latitude=="" || longitude=="" ||categoria==""|| viewModel.imagePath.value==null) {
                            Toast.makeText(
                                contexto,
                                "Preencha todos os campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else if(latitude.toDouble() < -90 || latitude.toDouble() > 90 || longitude.toDouble() < -180 || longitude.toDouble() > 180){
                            Toast.makeText(
                                contexto,
                                "Valores de coordenadas inválidas",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else {
                            Toast.makeText(
                                contexto,
                                "Local de interesse editado com sucesso",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.updateLocalInteresse_firebase(
                                descricao, categoria, latitude, longitude, metodo
                            )
                            navController.navigate("Interests")
                        }
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