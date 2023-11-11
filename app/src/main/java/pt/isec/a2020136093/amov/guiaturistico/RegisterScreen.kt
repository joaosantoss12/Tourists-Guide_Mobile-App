package pt.isec.a2020136093.amov.guiaturistico

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(
        navController: NavController,
        modifier : Modifier = Modifier
) {
        val userName = remember { mutableStateOf("") }
        val userEmail = remember { mutableStateOf("") }
        val userPassword = remember { mutableStateOf("") }

        Column(
                modifier = modifier
                        .padding(16.dp)
        ) {

                Text(
                        text = "REGISTER SCREEN",
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                )

                OutlinedTextField(
                        value = userName.value,
                        onValueChange = {
                                userName.value = it
                        },
                        label = { Text(text = stringResource(R.string.name_title)) },
                        modifier = Modifier
                                .fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

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
                        Text(text = stringResource(R.string.register_button))
                }
        }
}


@Preview
@Composable
fun RegisterScreenPreview() {
        //RegisterScreen()
}