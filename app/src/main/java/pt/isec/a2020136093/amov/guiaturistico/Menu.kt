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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text= title,
            fontSize = 48.sp,
            //fontFamily = SketchesFont,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxHeight()
                .wrapContentWidth()
                .background(Color.DarkGray)
                .padding(4.dp)
                .background(Color.LightGray)
                .padding(24.dp)
                .widthIn(200.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.75f)
        ) {
            for(btnName in options)
                Button(
                    onClick = { navController?.navigate(btnName) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(160,160,160)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = btnName,
                        //fontFamily = SketchesFont,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(16.dp)
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