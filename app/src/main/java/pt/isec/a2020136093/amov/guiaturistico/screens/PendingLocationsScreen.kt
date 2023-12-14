package pt.isec.a2020136093.amov.guiaturistico.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

@Composable
fun PendingLocationsScreen(
    viewModel : FirebaseViewModel,
    navController: NavController
) {
    viewModel.getLocations()
    val localidades = FirebaseViewModel.locations.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 0.dp),
    ) {
        Text(
            text = stringResource(R.string.title_pending_locations),
            textAlign = TextAlign.Center,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.font)),
            color = Color(42, 54, 66, 255),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 20.dp)
        )

        localidades.value?.forEach { (firstInfo, secondInfo) ->
            val (nome, descricao, imagemURL) = firstInfo
            val (coordenadas, email) = secondInfo

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
                        model = imagemURL,
                        error = painterResource(id = R.drawable.error),
                        contentDescription = "city image",
                    )
                    Text(
                        text = nome,
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
                        text = descricao,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        maxLines = 3,
                        fontFamily = FontFamily.Serif,
                        fontSize = 13.sp,
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
                            FirebaseViewModel._currentLocation.value = nome
                            navController.navigate("Interests")
                        }) {
                            Text(text = "Locais de interesse")
                        }

                        if(viewModel.user.value?.email == email){
                            Spacer(modifier = Modifier.width(10.dp))

                            Button(onClick = {
                                viewModel.tipoEditForm.value = "Localização"
                                viewModel.editName = nome
                                navController.navigate("EditForm")
                            }) {
                                Icon(
                                    Icons.Filled.Edit,
                                    "edit"
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
