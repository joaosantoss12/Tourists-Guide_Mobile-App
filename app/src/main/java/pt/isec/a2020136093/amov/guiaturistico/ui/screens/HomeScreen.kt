package pt.isec.a2020136093.amov.guiaturistico.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.RegularFont
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

import coil.compose.AsyncImage
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FirebaseViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight

        val user by remember { viewModel.user }
        LaunchedEffect(key1 = user) {
            if (user == null)
                onLogout()
        }

        val contexto = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.getLocations()
        }
        val localidades = FirebaseViewModel.locations.observeAsState()

        viewModel.selectedCategory = ""

        val filtersList = listOf(
            "A-Z",
            "Z-A",
            stringResource(R.string.distance_close_to_far),
            stringResource(R.string.distance_far_to_close)
        )

        var expanded by remember { mutableStateOf(false) }
        val none = stringResource(R.string.none)
        var selectedItem by remember { mutableStateOf(none) }



        Column(     // EM TELEMOVEIS DARK MODE FICAVA UMA MARGEM PRETA
            modifier = Modifier
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = { viewModel.signOut() },
                        modifier = Modifier
                            .padding(14.dp),

                        colors = ButtonDefaults.buttonColors(

                            containerColor = Color(185, 41, 41, 255), // Cor de fundo do botão
                            contentColor = Color.White // Cor do texto do botão
                        ),
                        shape = RoundedCornerShape(15.dp) // Borda arredondada do botão
                    ) {
                        Text(text = "Logout")
                    }

                    Button(
                        onClick = {
                            LocationViewModel._showLocations.value = true
                            navController.navigate("Map")
                        },
                        modifier = Modifier
                            .padding(14.dp),
                        colors = ButtonDefaults.buttonColors(

                            containerColor = Color(0, 80, 150, 255), // Cor de fundo do botão
                            contentColor = Color.White // Cor do texto do botão
                        ),
                        shape = RoundedCornerShape(15.dp) // Borda arredondada do botão
                    ) {
                        Text(text = stringResource(R.string.map))
                    }
                }


                Text(
                    text = stringResource(R.string.locations),
                    textAlign = TextAlign.Center,
                    fontSize = if (isLandscape) 20.sp else 35.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.font)),
                    color = Color(42, 54, 66, 255),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = if (isLandscape) 0.dp else 16.dp,
                            top = 0.dp,
                            end = if (isLandscape) 0.dp else 16.dp,
                            bottom = if (isLandscape) 4.dp else 20.dp
                        )
                )

                Column(

                ) {
                   if(!isLandscape) {
                       ExposedDropdownMenuBox(
                           expanded = expanded,
                           onExpandedChange = { expanded = !expanded },
                       ) {
                           TextField(
                               value = selectedItem,
                               onValueChange = {},
                               readOnly = true,
                               trailingIcon = {
                                   ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                               },
                               colors = ExposedDropdownMenuDefaults.textFieldColors(),
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .menuAnchor()
                                   .background(Color.White)
                           )

                           ExposedDropdownMenu(
                               expanded = expanded,
                               onDismissRequest = { expanded = false }
                           ) {

                               filtersList.forEach { filter ->
                                   DropdownMenuItem(
                                       text = {
                                           Text(
                                               text = filter,
                                               fontSize = if (isLandscape) 9.sp else 17.sp,
                                           )
                                       },
                                       onClick = {
                                           selectedItem = filter
                                           expanded = false

                                           when (selectedItem) {
                                               "A-Z" -> {
                                                   localidades.value?.sortBy { it.nome }
                                               }

                                               "Z-A" -> {
                                                   localidades.value?.sortByDescending { it.nome }
                                               }

                                               "Distância ▲" -> {
                                                   localidades.value?.sortBy { it.distance }
                                               }

                                               "Distância ▼" -> {
                                                   localidades.value?.sortByDescending { it.distance }
                                               }

                                               "Distance ▲" -> {
                                                   localidades.value?.sortBy { it.distance }
                                               }

                                               "Distance ▼" -> {
                                                   localidades.value?.sortByDescending { it.distance }
                                               }
                                           }
                                       },
                                   )
                               }
                           }
                       }
                   }



                    Column(
                        modifier = Modifier
                            .padding(0.dp, 5.dp, 0.dp, 0.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        Button(
                            onClick = {
                                navController.navigate("PendingLocations")
                            },
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(

                                containerColor = Color(0, 80, 150, 255), // Cor de fundo do botão
                                contentColor = Color.White // Cor do texto do botão
                            ),
                            shape = RoundedCornerShape(15.dp) // Borda arredondada do botão
                        )
                        {
                            Text(text = stringResource(R.string.localizacoes_pendentes))
                        }


                        localidades.value?.forEach { localizacao ->
                            if (localizacao.estado == "aprovado" || localizacao.estado == "pendente:apagar") {

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    if(isLandscape) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                        ) {

                                            AsyncImage(
                                                model = localizacao.imagemURL,
                                                error = painterResource(id = R.drawable.error),
                                                contentDescription = "city image",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxWidth(0.5f)
                                                    .heightIn(0.dp, 200.dp)
                                            )
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = localizacao.nome,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(0.dp, 10.dp, 0.dp, 0.dp),
                                                    textAlign = TextAlign.Center,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = FontFamily.Serif,
                                                    fontSize = 15.sp,
                                                    color = Color.Black
                                                )

                                                Text(
                                                    text = localizacao.descricao,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(8.dp),
                                                    maxLines = 5,
                                                    fontFamily = FontFamily.Serif,
                                                    fontSize = 11.sp,
                                                    color = Color.Gray
                                                )

                                                Spacer(modifier = Modifier.height(6.dp))

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(0.dp, 0.dp, 0.dp, 10.dp),
                                                    horizontalArrangement = Arrangement.Center
                                                ) {

                                                    OutlinedButton(onClick = {
                                                        FirebaseViewModel._currentLocation.value =
                                                            localizacao.nome
                                                        navController.navigate("Interests")
                                                    }) {
                                                        Text(text = stringResource(R.string.locais_de_interesse))
                                                    }
                                                }
                                            }
                                        }
                                    }else {

                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                        ) {

                                            AsyncImage(
                                                model = localizacao.imagemURL,
                                                error = painterResource(id = R.drawable.error),
                                                contentDescription = "city image",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(0.dp, 200.dp)
                                            )
                                            Text(
                                                text = localizacao.nome,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(0.dp, 10.dp, 0.dp, 0.dp),
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 18.sp,
                                                color = Color.Black
                                            )

                                            Text(
                                                text = localizacao.descricao,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(15.dp),
                                                maxLines = 5,
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(0.dp, 10.dp, 0.dp, 20.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {

                                                OutlinedButton(onClick = {
                                                    FirebaseViewModel._currentLocation.value =
                                                        localizacao.nome
                                                    navController.navigate("Interests")
                                                }) {
                                                    Text(text = stringResource(R.string.locais_de_interesse))
                                                }


                                                if (viewModel.user.value?.email == localizacao.email) {
                                                    Spacer(modifier = Modifier.width(10.dp))

                                                    Button(onClick = {
                                                        viewModel.tipoEditForm.value = "Localização"
                                                        viewModel.editName = localizacao.nome
                                                        navController.navigate("EditForm")
                                                    }) {
                                                        Icon(
                                                            Icons.Filled.Edit,
                                                            "edit"
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Button(onClick = {
                                                        viewModel.deleteLocalizacao(localizacao.nome)
                                                        Toast.makeText(
                                                            contexto,
                                                            "Localização apagada caso não possua locais de interesse!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }) {
                                                        Icon(
                                                            Icons.Filled.Delete,
                                                            "delete"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }


                        Button(
                            onClick = {
                                navController.navigate("AddForm")
                                viewModel.tipoAddForm.value = "Localização"
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(

                                containerColor = Color(76, 175, 80, 255), // Cor de fundo do botão
                                contentColor = Color.White // Cor do texto do botão
                            ),
                            shape = RoundedCornerShape(15.dp) // Borda arredondada do botão
                        )
                        {
                            Text(text = stringResource(R.string.adicionaLocal))
                        }
                    }
                }           // END OF HOME SCREEN COLUMN
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    //LoginScreen()
}