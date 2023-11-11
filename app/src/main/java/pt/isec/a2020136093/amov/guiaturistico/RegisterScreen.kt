package pt.isec.a2020136093.amov.guiaturistico

import android.R.attr.fontWeight
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        text = stringResource(R.string.createAccount),
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        style = TextStyle(
                                color = Color(10, 10, 150),
                                fontFamily = FontFamily.Serif,
                                fontSize = 30.sp, // Tamanho do texto
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center // Centraliza o texto



                        )
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
                                .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                                containerColor = Color(10, 10, 150), // Cor de fundo do botão
                                contentColor = Color.White // Cor do texto do botão
                        ),
                        shape = RoundedCornerShape(15.dp) // Borda arredondada do botão
                ) {
                        Text(
                                text = stringResource(R.string.register_button),
                                //style = MaterialTheme.typography.button
                        )
                }

                Text(
                        text = buildAnnotatedString {
                                append(stringResource(R.string.already_have_account))
                                withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                                        append(" ${stringResource(R.string.login)}")
                                }
                        },
                        modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                        navController.navigate("Login")
                                }
                                .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                )

        }
}


@Preview
@Composable
fun RegisterScreenPreview() {
        //RegisterScreen()
}