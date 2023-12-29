package pt.isec.a2020136093.amov.guiaturistico.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

@Composable
fun PendingCategoriesScreen(
    viewModel: FirebaseViewModel,
    navController: NavController
) {

    viewModel.getCategorias()
    val categorias = FirebaseViewModel.categorias


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.title_categorias_pendentes),
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
            modifier = Modifier
                .padding(0.dp, 15.dp, 0.dp, 0.dp)
                .verticalScroll(rememberScrollState())
        ) {
            categorias.value?.forEach { categoria ->

                if (categoria.estado == "pendente") {

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
                                model = categoria.imagemURL,
                                error = painterResource(id = R.drawable.error),
                                contentDescription = "local image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(0.dp, 200.dp)
                            )
                            Text(
                                text = categoria.nome,
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
                                text = categoria.descricao,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                maxLines = 3,
                                fontFamily = FontFamily.Serif,
                                fontSize = 13.sp,
                                color = Color.Gray
                            )

                            if(viewModel.user.value?.email != categoria.email && (categoria.emailVotosAprovar?.contains(viewModel.user.value?.email) == false || categoria.emailVotosAprovar == null)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 10.dp, 0.dp, 20.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.voteToAproveCategories(categoria.nome)
                                        },
                                    ) {
                                        Text(text = "Aprovar [${categoria.emailVotosAprovar?.size ?: 0}/2]")
                                    }
                                }
                            }
                            else{
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 10.dp, 0.dp, 20.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.voteToAproveCategories(categoria.nome)
                                        },
                                    ) {
                                        Text(text = "Aprovar [${categoria.emailVotosAprovar?.size ?: 0}/2]")
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
