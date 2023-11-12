package pt.isec.a2020136093.amov.guiaturistico

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier : Modifier = Modifier
) {

    val filtersListAlfabeto = listOf(
        "A-Z",
        "Z-A",
    )
    val filtersListDistancia = listOf(
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
            text = stringResource(R.string.locations),
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


            Column(

            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp, 0.dp, 0.dp)
                ) {
                    filtersListAlfabeto.forEach { filter ->
                        Button(
                            onClick = { selectedItem = filter },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(10, 10, 150), // Cor de fundo do botão
                                contentColor = Color.White // Cor do texto do botão
                            ),
                        ) {
                            Text(
                                text = filter
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    filtersListDistancia.forEach { filter ->
                        Button(
                            onClick = { selectedItem = filter },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(10, 10, 150), // Cor de fundo do botão
                                contentColor = Color.White // Cor do texto do botão
                            ),
                        ) {
                            Text(
                                text = filter
                            )
                        }
                    }
                }
            }




            Column(
                modifier = Modifier
                    .padding(0.dp, 20.dp, 0.dp, 0.dp)
                    .verticalScroll(rememberScrollState())
            ) {


                Card(modifier= Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.lisboa),contentDescription = null)
                        Text(text = "Lisboa",
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
                            .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { /*TODO*/ }) {
                                Text(text ="Ver localizacões")
                            }
                        }

                    }
                } //LISBOA CARD

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier= Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.porto),contentDescription = null)
                        Text(text = "Porto",
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
                            .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { /*TODO*/ }) {
                                Text(text ="Ver localizacões")
                            }
                        }

                    }
                } //PORTO CARD

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier= Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.coimbra),contentDescription = null)
                        Text(text = "Coimbra",
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
                            .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { /*TODO*/ }) {
                                Text(text ="Ver localizacões")
                            }
                        }
                    }
                } //COIMBRA CARD

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier= Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.faro),contentDescription = null)
                        Text(text = "Faro",
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
                            .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { /*TODO*/ }) {
                                Text(text ="Ver localizacões")
                            }
                        }

                    }
                } //FARO CARD

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier= Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                    elevation= CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor= Color.White
                    )
                ) {
                    Column (modifier= Modifier.fillMaxSize()){
                        Image(painter= painterResource(R.drawable.aveiro),contentDescription = null)
                        Text(text = "Aveiro",
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
                            .padding(0.dp, 10.dp, 0.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center){
                            OutlinedButton(onClick = { /*TODO*/ }) {
                                Text(text ="Ver localizacões")
                            }
                        }
                    }
                } //AVEIRO CARD





                /* Column(
                     modifier = Modifier
                         .padding(0.dp, 10.dp, 0.dp, 0.dp)
                 ) {
                     Text(
                         textAlign = TextAlign.Center,
                         text = "•┈••✦ Coimbra ✦••┈•",
                         fontWeight = FontWeight.Bold,
                         fontFamily = FontFamily.Serif,
                         fontSize = 20.sp,
                         modifier = Modifier
                             .fillMaxWidth()
                     )
                     Image(
                         painter = painterResource(R.drawable.coimbra),
                         contentDescription = null,
                         modifier = Modifier
                             .fillMaxWidth()
                     )
                 }       // COIMBRA COLUMN

                 Column(
                     modifier = Modifier
                         .padding(0.dp, 30.dp, 0.dp, 0.dp)
                 ) {
                     Text(
                         textAlign = TextAlign.Center,
                         text = "•┈••✦ Lisboa ✦••┈•",
                         fontWeight = FontWeight.Bold,
                         fontFamily = FontFamily.Serif,
                         fontSize = 20.sp,
                         modifier = Modifier
                             .fillMaxWidth()
                     )
                     Image(
                         painter = painterResource(R.drawable.lisboa),
                         contentDescription = null,
                         modifier = Modifier
                             .fillMaxWidth()
                     )
                 }       // LISBON COLUMN

                 Column(
                     modifier = Modifier
                         .padding(0.dp, 30.dp, 0.dp, 0.dp)
                 ) {
                     Text(
                         textAlign = TextAlign.Center,
                         text = "•┈••✦ Porto ✦••┈•",
                         fontWeight = FontWeight.Bold,
                         fontFamily = FontFamily.Serif,
                         fontSize = 20.sp,
                         modifier = Modifier
                             .fillMaxWidth()
                     )
                     Image(
                         painter = painterResource(R.drawable.porto),
                         contentDescription = null,
                         modifier = Modifier
                             .fillMaxWidth()
                     )
                 }       // PORTO COLUMN*/



            }


        }           // END OF HOME SCREEN COLUMN


    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    //LoginScreen()
}