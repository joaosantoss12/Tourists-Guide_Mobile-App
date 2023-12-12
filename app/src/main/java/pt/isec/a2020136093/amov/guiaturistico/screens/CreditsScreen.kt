package pt.isec.a2020136093.amov.guiaturistico.screens

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CreditsScreen(
    navController: NavController,
    modifier : Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = "CRÉDITOS",
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Beatriz Isabel Inácio Maia",
            textAlign = TextAlign.Center,
            fontSize = 23.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = "2020128841",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "João Miguel Duarte dos Santos",
            textAlign = TextAlign.Center,
            fontSize = 23.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = "2020136093",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "APLICAÇÕES MÓVEIS", // Replace with the subject name
            fontSize = 30.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Preview
@Composable
fun CreditsScreenPreview() {
    //CreditsScreen()
}