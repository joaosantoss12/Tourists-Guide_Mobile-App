package pt.isec.a2020136093.amov.guiaturistico.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.checkerframework.checker.units.qual.min
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.ui.theme.RegularFont
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel


@Composable
fun RegisterScreen(
        viewModel : FirebaseViewModel,
        navController: NavController,
        modifier : Modifier = Modifier,
        onSuccess : () -> Unit
) {
        BoxWithConstraints {
                val isLandscape = maxWidth > maxHeight
                val spacerHeight = if (isLandscape) 4.dp else 16.dp
                val userName = remember { mutableStateOf("") }
                val userEmail = remember { mutableStateOf("") }
                val userPassword = remember { mutableStateOf("") }

                val error by remember { viewModel.error }
                val user by remember { viewModel.user }
                LaunchedEffect(key1 = user){
                        if(user != null && error == null)
                                onSuccess()
                }

                Column(
                        modifier = modifier
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                                .background(Color.White)
                ) {
                        if (!isLandscape) {
                                Image(
                                        painter = painterResource(R.drawable.imagem2),
                                        contentDescription = null,
                                        modifier = Modifier
                                                .fillMaxWidth()
                                )
                        }

                        Text(
                                text = stringResource(R.string.createAccount),
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                style = TextStyle(
                                        color = Color(0, 80, 150, 255),
                                        fontFamily = RegularFont,
                                        fontSize = if (isLandscape) 16.sp else 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center // Centraliza o texto

                                )
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

                        Spacer(Modifier.height(spacerHeight))

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

                        Spacer(Modifier.height(spacerHeight))

                        if(error != null){
                                Text("Error: $error", Modifier.background(Color(255,0,0)))
                                Spacer(Modifier.height(spacerHeight))
                        }

                        Spacer(Modifier.height(spacerHeight))

                        Button(
                                onClick = { viewModel.createUserWithEmail(userEmail.value, userPassword.value) },
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),

                        colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0, 80, 150, 255), // Cor de fundo do botão
                                        contentColor = Color.White // Cor do texto do botão
                                ),
                                shape = RoundedCornerShape(15.dp) // Borda arredondada do botão
                        ) {
                                Text(
                                        text = stringResource(R.string.register),
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
}