package pt.isec.a2020136093.amov.guiaturistico.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.RegularFont
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestsScreen(
    viewModel: FirebaseViewModel,
    navController: NavController,
) {

    LaunchedEffect(Unit) {
        viewModel.getCategorias()
        viewModel.getLocaisInteresse()
    }

    // Observe the state changes
    val categorias = FirebaseViewModel.categorias.observeAsState()
    val locaisInteresse = FirebaseViewModel.locaisInteresse.observeAsState()

    val context = LocalContext.current

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
            Button(
                onClick = {
                    LocationViewModel._showLocations.value = false
                    navController.navigate("Map")
                },
                modifier = Modifier
                    .padding(14.dp)
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(

                    containerColor = Color(0, 80, 150, 255), // Cor de fundo do botão
                    contentColor = Color.White // Cor do texto do botão
                ),
                shape = RoundedCornerShape(15.dp) // Borda arredondada do botão
            ) {
                Text(text =  stringResource(R.string.map))
            }


            Text(
                text = stringResource(R.string.points_of_interest),
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
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
                                    when(selectedItem) {
                                        "A-Z" -> {
                                            locaisInteresse.value?.sortBy { it.nome }
                                        }

                                        "Z-A" -> {
                                            locaisInteresse.value?.sortByDescending { it.nome }
                                        }

                                        "Distância ▲" -> {
                                            locaisInteresse.value?.sortBy { it.distance }
                                        }

                                        "Distância ▼" -> {
                                            locaisInteresse.value?.sortByDescending { it.distance }
                                        }

                                        "Distance ▲" -> {
                                            locaisInteresse.value?.sortBy { it.distance }
                                        }

                                        "Distance ▼" -> {
                                            locaisInteresse.value?.sortByDescending { it.distance }
                                        }
                                    }
                                },
                            )
                        }

                    }
                }

                Column(
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 0.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    LazyRow(

                    ) {
                        item{
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(color = Color.White)
                                    .size(120.dp)
                                    .clickable {
                                        viewModel.selectedCategory = ""
                                        viewModel.getLocaisInteresse()
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.all1),
                                        contentDescription = "all categories image",
                                        contentScale = ContentScale.FillBounds,
                                    )

                                    Text(
                                        text = stringResource(R.string.all_categories),
                                        style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            shadow = Shadow(
                                                color = Color.Black, offset = Offset(3.0f, 4.0f), blurRadius = 1f
                                            )
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }

                        categorias.value?.forEach { categoria ->

                            if (categoria.estado == "aprovado") {

                                item {

                                    Card(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .background(color = Color.White)
                                            .size(120.dp)
                                            .clickable {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        categoria.descricao,
                                                        Toast.LENGTH_LONG,
                                                    )
                                                    .show()
                                                viewModel.selectedCategory = categoria.nome
                                                viewModel.getLocaisInteresse()
                                            }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                        ) {
                                            AsyncImage(
                                                model = categoria.imagemURL,
                                                error = painterResource(id = R.drawable.error),
                                                contentDescription = "category image",
                                                contentScale = ContentScale.FillBounds,
                                            )

                                            if (viewModel.user.value?.email == categoria.email) {
                                                Button(
                                                    onClick = {
                                                        viewModel.tipoEditForm.value = "Categoria"
                                                        viewModel.editName = categoria.nome
                                                        navController.navigate("EditForm")
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(185, 0, 0, 255)
                                                    ),
                                                    modifier = Modifier
                                                        .align(Alignment.TopStart)
                                                        .scale(0.75f)
                                                ) {
                                                    Icon(
                                                        Icons.Filled.Edit,
                                                        "edit"
                                                    )
                                                }

                                                Button(
                                                    onClick = {
                                                        viewModel.deleteCategoria(categoria.nome)
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(185, 0, 0, 255)
                                                    ),
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .scale(0.75f)
                                                ) {
                                                    Icon(
                                                        Icons.Filled.Delete,
                                                        contentDescription = "delete",
                                                    )
                                                }
                                            }

                                            Text(
                                                text = categoria.nome,
                                                style = TextStyle(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    shadow = Shadow(
                                                        color = Color.Black, offset = Offset(3.0f, 4.0f), blurRadius = 1f
                                                    )
                                                ),
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(120.dp)
                                    .clickable { navController.navigate("PendingCategories") }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color(0, 80, 150, 255))
                                ) {
                                    AsyncImage(
                                        model = R.drawable.info,
                                        error = painterResource(id = R.drawable.error),
                                        contentDescription = "category image",
                                        contentScale = ContentScale.FillHeight,
                                        modifier = Modifier
                                            .padding(35.dp)
                                    )

                                    Text(
                                        text = stringResource(R.string.pending_categories_card),
                                            style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            shadow = Shadow(
                                                color = Color.Black, offset = Offset(3.0f, 4.0f), blurRadius = 1f
                                            )
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                        item {
                            AddCategoryButton(
                                onClick = {
                                    navController.navigate("AddForm")
                                    viewModel.tipoAddForm.value = "Categoria"
                                },
                            )
                        }
                    }

                    Button(
                        onClick = {
                            navController.navigate("PendingInterests")
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
                        Text(text = stringResource(R.string.locais_interesse_pendentes))
                    }



                    locaisInteresse.value?.forEach { localInteresse ->

                        if (localInteresse.estado == "aprovado" || localInteresse.estado == "pendente:apagar") {
                            if(viewModel.selectedCategory == ""){
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        AsyncImage(
                                            model = localInteresse.imagemURL,
                                            error = painterResource(id = R.drawable.error),
                                            contentDescription = "local image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .heightIn(0.dp, 200.dp)
                                        )
                                        Text(
                                            text = localInteresse.nome,
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
                                            text = localInteresse.descricao,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(15.dp),
                                            maxLines = 3,
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )

                                        Text(
                                            text = localInteresse.classificacao + " ★",
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 15.sp,
                                            color = Color.Black
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,

                                            ) {
                                            for (i in 1..4) {
                                                OutlinedButton(
                                                    onClick = {
                                                        viewModel.updateClassificacao_firebase(
                                                            localInteresse.nome,
                                                            (i - 1).toString()
                                                        )
                                                    },
                                                    modifier = Modifier
                                                        .padding(horizontal = 5.dp),
                                                ) {
                                                    Text(text = (i - 1).toString())
                                                }
                                            }
                                        }


                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(0.dp, 10.dp, 0.dp, 20.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            OutlinedButton(onClick = {
                                                FirebaseViewModel._currentLocalInteresse.value =
                                                    localInteresse.nome
                                                navController.navigate("Comments")
                                            }) {
                                                Text(text = stringResource(R.string.comments))
                                            }

                                            if (localInteresse.estado == "pendente:apagar" && viewModel.user.value?.email != localInteresse.email && (localInteresse.emailVotosEliminar?.contains(
                                                    viewModel.user.value?.email
                                                ) == false || localInteresse.emailVotosEliminar == null)
                                            ) {
                                                Spacer(modifier = Modifier.width(10.dp))

                                                Button(onClick = {
                                                    viewModel.voteToDelete(localInteresse.nome)
                                                }) {
                                                    Icon(
                                                        Icons.Filled.Close,
                                                        "voteDelete"
                                                    )
                                                }
                                            }


                                            if (viewModel.user.value?.email == localInteresse.email) {
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Button(onClick = {
                                                    viewModel.tipoEditForm.value =
                                                        "Local de Interesse"
                                                    viewModel.editName = localInteresse.nome
                                                    navController.navigate("EditForm")
                                                }) {
                                                    Icon(
                                                        Icons.Filled.Edit,
                                                        "edit"
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(10.dp))
                                                Button(onClick = {
                                                    viewModel.deleteLocalInteresse(localInteresse.nome)
                                                }) {
                                                    Icon(
                                                        Icons.Filled.Delete,
                                                        "delete"
                                                    )
                                                }
                                            }
                                        }

                                    }
                                } //CARD
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            else if (localInteresse.categoria == viewModel.selectedCategory) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Column(modifier = Modifier.fillMaxSize()) {
                                        AsyncImage(
                                            model = localInteresse.imagemURL,
                                            error = painterResource(id = R.drawable.error),
                                            contentDescription = "local image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .heightIn(0.dp, 200.dp)
                                        )
                                        Text(
                                            text = localInteresse.nome,
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
                                            text = localInteresse.descricao,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(15.dp),
                                            maxLines = 3,
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )

                                        Text(
                                            text = localInteresse.classificacao + " ★",
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 15.sp,
                                            color = Color.Black
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,

                                            ) {
                                            for (i in 1..4) {
                                                OutlinedButton(
                                                    onClick = {
                                                        viewModel.updateClassificacao_firebase(
                                                            localInteresse.nome,
                                                            (i - 1).toString()
                                                        )
                                                    },
                                                    modifier = Modifier
                                                        .padding(horizontal = 5.dp),
                                                ) {
                                                    Text(text = (i - 1).toString())
                                                }
                                            }
                                        }


                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(0.dp, 10.dp, 0.dp, 20.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            OutlinedButton(onClick = {
                                                FirebaseViewModel._currentLocalInteresse.value =
                                                    localInteresse.nome
                                                navController.navigate("Comments")
                                            }) {
                                                Text(text = "Comentários")
                                            }

                                            if (localInteresse.estado == "pendente:apagar" && viewModel.user.value?.email != localInteresse.email && (localInteresse.emailVotosEliminar?.contains(
                                                    viewModel.user.value?.email
                                                ) == false || localInteresse.emailVotosEliminar == null)
                                            ) {
                                                Spacer(modifier = Modifier.width(10.dp))

                                                Button(onClick = {
                                                    viewModel.voteToDelete(localInteresse.nome)
                                                }) {
                                                    Icon(
                                                        Icons.Filled.Close,
                                                        "voteDelete"
                                                    )
                                                }
                                            }


                                            if (viewModel.user.value?.email == localInteresse.email) {
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Button(onClick = {
                                                    viewModel.tipoEditForm.value =
                                                        "Local de Interesse"
                                                    viewModel.editName = localInteresse.nome
                                                    navController.navigate("EditForm")
                                                }) {
                                                    Icon(
                                                        Icons.Filled.Edit,
                                                        "edit"
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(10.dp))
                                                Button(onClick = {
                                                    viewModel.deleteLocalInteresse(localInteresse.nome)
                                                }) {
                                                    Icon(
                                                        Icons.Filled.Delete,
                                                        "delete"
                                                    )
                                                }
                                            }
                                        }

                                    }
                                } //CARD
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }

                    Button(
                        onClick = { navController.navigate("AddForm")
                            viewModel.tipoAddForm.value = "Local de Interesse" },
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
                        Text(text = stringResource(R.string.adicionaLocalInteresse))
                    }
                }
            }           // END OF HOME SCREEN COLUMN
        }
    }
}

@Composable
fun AddCategoryButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(
                Color(0, 80, 150, 255),
                shape = RoundedCornerShape(8.dp)
            )
            .height(120.dp)
            .width(120.dp)
            .clip(shape = RoundedCornerShape(8.dp))
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Center)
        )
    }
}