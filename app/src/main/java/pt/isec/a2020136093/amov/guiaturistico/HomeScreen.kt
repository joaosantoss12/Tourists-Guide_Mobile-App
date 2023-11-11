package pt.isec.a2020136093.amov.guiaturistico

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier : Modifier = Modifier
) {

    val filtersList = listOf("A-Z", "Z-A", stringResource(R.string.distance_close_to_far), stringResource(R.string.distance_far_to_close))
    var expanded by remember { mutableStateOf(false) }
    val none = stringResource(R.string.none)
    var selectedItem by remember { mutableStateOf(none) }


    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Text(
            text = "HOME SCREEN",
            textAlign = TextAlign.Center,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // box
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier
                .padding(0.dp,20.dp,0.dp,0.dp)
        ) {
            // text field
            TextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(text = stringResource(R.string.filters_title))
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            // menu
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                filtersList.forEach { selectedOption ->

                    DropdownMenuItem(
                        text = { Text(text = selectedOption) },
                        onClick = {
                            selectedItem = selectedOption
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    //LoginScreen()
}