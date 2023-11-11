package pt.isec.a2020136093.amov.guiaturistico

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
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
    vararg options: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 40.dp, 0.dp, 0.dp),
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            lineHeight = 45.sp,
            fontSize = 50.sp,
            color = Color.Black,
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
            for (btnName in options)
                Button(
                    onClick = { navController?.navigate(btnName) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(10, 10, 150)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 25.dp, 0.dp, 10.dp)
                ) {
                    Text(
                        text = btnName,
                        //fontFamily = SketchesFont,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(15.dp)
                    )
                }
        }
    }
}

@Preview
@Composable
fun MenuPreview() {
    Menu("Sketches", null, "Solid", "Gallery", "Camera", "List")
}