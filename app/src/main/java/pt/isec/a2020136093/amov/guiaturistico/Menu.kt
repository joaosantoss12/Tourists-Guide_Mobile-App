package pt.isec.a2020136093.amov.guiaturistico
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
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
    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isLandscape) 16.dp else 0.dp, 40.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isLandscape) {
                // Portrait layout
                Image(
                    painter = painterResource(R.drawable.mainimagem),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.35f),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = title,
                textAlign = TextAlign.Center,
                lineHeight = if (isLandscape) 25.sp else 45.sp,
                fontSize = if (isLandscape) 25.sp else 40.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.font)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 31.dp, 16.dp, 16.dp)
            )
            MenuButtons(navController, isLandscape)
        }
    }
}

@Composable
fun MenuButtons(navController: NavHostController?, isLandscape: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth( 0.6f)
            .fillMaxHeight()
    ) {
        Button(
            onClick = { navController?.navigate("Register") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0, 80, 150, 255)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 5.dp, 0.dp, 5.dp)
        ) {
            Text(
                text = stringResource(R.string.register),
                fontSize = if (isLandscape) 18.sp else 20.sp,
                modifier = Modifier.padding(0.dp, 3.dp, 0.dp, 3.dp)
            )
        }

        Button(
            onClick = { navController?.navigate("Login") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0, 80, 150, 255)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 10.dp)
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = if (isLandscape) 18.sp else 20.sp,
                modifier = Modifier.padding(0.dp, 3.dp, 0.dp, 3.dp)
            )
        }

        Button(
            onClick = { navController?.navigate("Credits") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0, 80, 150, 255)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 10.dp)
        ) {
            Text(
                text = stringResource(R.string.credits),
                fontSize = if (isLandscape) 18.sp else 20.sp,
                modifier = Modifier.padding(0.dp, 3.dp, 0.dp, 3.dp)
            )
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
                    colors = listOf(Color.White, Color(0, 80, 150, 255)),
                )
            )
        }
    }
}
