package pt.isec.a2020136093.amov.guiaturistico.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel.Companion.categorias
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun CommentsScreen(
    viewModel: FirebaseViewModel,
    navController: NavController
) {

    viewModel.getComentarios()
    val comentarios = FirebaseViewModel.comentarios.observeAsState()

    val comentarioAtual = remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.comentarios),
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

            Column(){
                OutlinedTextField(
                    value = comentarioAtual.value,
                    onValueChange = {
                        if (it.length <= 200)
                            comentarioAtual.value = it
                    },
                    label = { Text(text = stringResource(R.string.escreva_um_comentario)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp, 0.dp, 20.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    OutlinedButton(
                        onClick = {
                            viewModel.addComentario(comentarioAtual.value)
                            comentarioAtual.value = ""
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.enviar),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            comentarios.value?.forEach { comentario ->

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

                            Text(
                                text = comentario.texto,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                maxLines = 3,
                                fontFamily = FontFamily.Serif,
                                fontSize = 13.sp,
                                color = Color.Black
                            )

                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                            ){
                                Text(
                                    text = comentario.email,
                                    color = Color.Gray
                                )

                                Text(
                                    text = comentario.dia + "/" + comentario.mes + "/" + comentario.ano,
                                    color = Color.Gray
                                )
                            }


                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestampToDate(timestamp: Long): String {

    val instant = Instant.ofEpochMilli(timestamp)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDateTime.format(formatter)
}