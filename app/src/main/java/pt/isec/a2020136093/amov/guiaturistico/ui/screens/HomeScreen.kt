package pt.isec.a2020136093.amov.guiaturistico.ui.screens

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

    viewModel.getLocations()
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

    when(selectedItem){
        "A-Z" -> {
            localidades.value?.sortBy { it.nome }
        }
        "Z-A" -> {
            localidades.value?.sortByDescending { it.nome }
        }
        "Distância ▲"->{
            FirebaseViewModel._locations.value?.sortBy { it.distance }
        }
        "Distância ▼"->{
            FirebaseViewModel._locations.value?.sortByDescending { it.distance }
        }
        "Distance ▲"->{
            FirebaseViewModel._locations.value?.sortBy { it.distance }
        }
        "Distance ▼"->{
            FirebaseViewModel._locations.value?.sortByDescending { it.distance }
        }
        none -> {}
    }



    val user by remember { viewModel.user }
    LaunchedEffect(key1 = user) {
        if (user == null)
            onLogout()
    }

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
                modifier= Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
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
                    Text(text = "Mapa")
                }
            }


            Text(
                text = stringResource(R.string.locations),
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.font)),
                color = Color(42, 54, 66, 255),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 20.dp)
            )

            Column(

            ) {
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
                                        fontSize = 17.sp,
                                    )
                                },
                                onClick = {
                                    selectedItem = filter
                                    expanded = false
                                    when(selectedItem){
                                        "A-Z" -> {
                                            FirebaseViewModel._locations.value?.sortBy { it.nome }
                                        }
                                        "Z-A" -> {
                                            FirebaseViewModel._locations.value?.sortByDescending { it.nome }
                                        }
                                        "Distância ▲"->{
                                            FirebaseViewModel._locations.value?.sortBy { it.distance }
                                        }
                                        "Distância ▼"->{
                                            FirebaseViewModel._locations.value?.sortByDescending { it.distance }
                                        }
                                        "Distance ▲"->{
                                            FirebaseViewModel._locations.value?.sortBy { it.distance }
                                        }
                                        "Distance ▼"->{
                                            FirebaseViewModel._locations.value?.sortByDescending { it.distance }
                                        }
                                    }
                                },
                            )
                        }
                    }
                }



                Column(
                    modifier = Modifier
                        .padding(0.dp, 18.dp, 0.dp, 0.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Button(
                        onClick = {
                            navController.navigate("PendingLocations")
                        },
                        modifier = Modifier
                            .padding(16.dp)
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
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {

                                    AsyncImage(
                                        model = localizacao.imagemURL,
                                        error = painterResource(id = R.drawable.error),
                                        contentDescription = "city image",
                                        //contentScale = ContentScale.FillWidth,
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
                                            FirebaseViewModel._currentLocation.value = localizacao.nome
                                            navController.navigate("Interests")
                                        }) {
                                            Text(text = "Locais de interesse")
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


@Preview
@Composable
fun HomeScreenPreview() {
    //LoginScreen()
}