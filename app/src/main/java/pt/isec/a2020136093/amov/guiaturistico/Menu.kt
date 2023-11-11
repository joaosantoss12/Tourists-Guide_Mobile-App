package pt.isec.a2020136093.amov.guiaturistico

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Menu(
    title: String,
    navController: NavHostController?,
) {
    GradientBackground()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 40.dp, 0.dp, 0.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.imagem3png), // Substitua "sua_imagem" pelo nome do seu recurso de imagem
            contentDescription = null, // Descrição da imagem (opcional)
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = title,
            textAlign = TextAlign.Center,
            lineHeight = 45.sp,
            fontSize = 50.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 50.dp, 0.dp, 0.dp)   // BOX DO TEXTO
                .padding(16.dp) // TEXTO
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.75f)
        ) {
            Column(

            ) {
                Button(
                    onClick = { navController?.navigate("Register") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(10, 10, 150) // Cor do texto do botão
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp, 0.dp, 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.register),
                        //fontFamily = SketchesFont,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(0.dp, 3.dp, 0.dp, 3.dp)
                    )
                }

                Button(
                    onClick = { navController?.navigate("Login") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(10, 10, 150) // Cor do texto do botão
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp, 0.dp, 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        //fontFamily = SketchesFont,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(0.dp, 3.dp, 0.dp, 3.dp)
                    )
                }

                Button(
                    onClick = { navController?.navigate("Credits") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(10, 10, 150) // Cor do texto do botão
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp, 0.dp, 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.credits),
                        //fontFamily = SketchesFont,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(0.dp, 3.dp, 0.dp, 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GradientBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(10, 10, 150)), // Cores para o degradê
                    startY = 0f,
                    endY = size.height * 0.99f
                )
            )
        }
    }
}

@Preview
@Composable
fun MenuPreview() {
    //Menu("Sketches", null, "Solid", "Gallery", "Camera", "List")
}