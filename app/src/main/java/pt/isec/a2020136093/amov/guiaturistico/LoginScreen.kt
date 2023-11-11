package pt.isec.a2020136093.amov.guiaturistico

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController,
    modifier : Modifier = Modifier
) {
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {

        Text(
            text = "LOGIN SCREEN",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        OutlinedTextField(
            value = userEmail.value,
            onValueChange = {
                userEmail.value = it
            },
            label = { Text(text = stringResource(R.string.email_title)) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = userPassword.value,
            onValueChange = {
                userPassword.value = it
            },
            label = { Text(text = stringResource(R.string.password_title)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("Home") },
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(text = stringResource(R.string.login_button))
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    //LoginScreen()
}