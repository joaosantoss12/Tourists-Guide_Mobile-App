package pt.isec.a2020136093.amov.guiaturistico

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestsScreen(
    navController: NavController,
) {
    val filtersList = listOf(
        "A-Z",
        "Z-A",
        stringResource(R.string.distance_close_to_far),
        stringResource(R.string.distance_far_to_close)
    )

    var expanded by remember { mutableStateOf(false) }
    val none = stringResource(R.string.none)
    var selectedItem by remember { mutableStateOf(none) }


    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    ){
        Text(
            text = stringResource(R.string.points_of_interest),
            lineHeight = 30.sp,
            textAlign = TextAlign.Center,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Column(

        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {expanded = !expanded},
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
                            },
                        )
                    }

                }
            }





            Column(
                modifier = Modifier
                    .padding(0.dp, 20.dp, 0.dp, 0.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                @Composable
                fun AddCategoryButton(onClick: () -> Unit) {
                    Box(
                        modifier = Modifier
                            .clickable(onClick = onClick)
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
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


                @Composable
                fun CategoryItem(category: String, imageResId: Int, onClick: () -> Unit) {
                    Box(
                        modifier = Modifier
                            .clickable(onClick = onClick)
                            .padding(8.dp)
                    ) {
                        // Add an Image with the category text overlay
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = null,
                            modifier = Modifier
                                .height(120.dp)
                                .width(120.dp)
                                .clip(shape = RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = category,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        )
                    }
                }


                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf(
                        "Museus" to R.drawable.museu1,
                        "Monumentos" to R.drawable.igrejap,
                        "Jardins" to R.drawable.jardim,
                        "Miradouros" to R.drawable.miradouro,
                        "Restaurantes" to R.drawable.restaurante,
                        "Alojamentos" to R.drawable.hotel,
                    )

                    items(categories) { (category, imageResId) ->
                        CategoryItem(category, imageResId) {
                            // Handle click on category, you can navigate or perform an action
                        }
                    }
                    item {
                        AddCategoryButton(onClick = {
                            // Handle click on the button, you can add logic to add a new category
                        })
                    }
                }


                Card(modifier= Modifier
                    .fillMaxWidth(),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.lisboa),contentDescription = null)
                        Text(text = "Museu do Fado",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color.Black)

                        Text(text = "Descrição",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            maxLines=3,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            color = Color.Gray)

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 10.dp, 0.dp, 20.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { navController.navigate("Interests") }) {
                                Text(text ="Selecionar")
                            }
                        }

                    }
                } //LISBOA CARD

                Spacer(modifier = Modifier.height(20.dp))

                Card(modifier= Modifier
                    .fillMaxWidth(),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.porto),contentDescription = null)
                        Text(text = "Museu do Vinho do Porto",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color.Black)

                        Text(text = "Descrição",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            maxLines=3,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            color = Color.Gray)

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 10.dp, 0.dp, 20.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { navController.navigate("Interests") }) {
                                Text(text ="Selecionar")
                            }
                        }

                    }
                } //PORTO CARD

                Spacer(modifier = Modifier.height(20.dp))

                Card(modifier= Modifier
                    .fillMaxWidth(),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.coimbra),contentDescription = null)
                        Text(text = "Universidade de Coimbra",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color.Black)

                        Text(text = "Descrição",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            maxLines=3,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            color = Color.Gray)

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 10.dp, 0.dp, 20.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { navController.navigate("Interests") }) {
                                Text(text ="Selecionar")
                            }
                        }
                    }
                } //COIMBRA CARD

                Spacer(modifier = Modifier.height(20.dp))

                Card(modifier= Modifier
                    .fillMaxWidth(),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.faro),contentDescription = null)
                        Text(text = "Campo Desportivo de Faro",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color.Black)

                        Text(text = "Descrição",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            maxLines=3,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            color = Color.Gray)

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 10.dp, 0.dp, 20.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { navController.navigate("Interests") }) {
                                Text(text ="Selecionar")
                            }
                        }

                    }
                } //FARO CARD

                Spacer(modifier = Modifier.height(20.dp))

                Card(modifier= Modifier
                    .fillMaxWidth(),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.aveiro),contentDescription = null)
                        Text(text = "Castelo de Aveiro",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color.Black)

                        Text(text = "Descrição",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            maxLines=3,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            color = Color.Gray)

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 10.dp, 0.dp, 20.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { navController.navigate("Interests") }) {
                                Text(text ="Selecionar")
                            }
                        }
                    }
                } //AVEIRO CARD

                Spacer(modifier = Modifier.height(20.dp))


                // MAIS CARDS





            }


        }           // END OF HOME SCREEN COLUMN



    }
}