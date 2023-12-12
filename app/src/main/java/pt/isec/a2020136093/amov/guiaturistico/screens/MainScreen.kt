package pt.isec.a2020136093.amov.guiaturistico.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isec.a2020136093.amov.guiaturistico.Menu
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

const val MENU_SCREEN = "Menu"
const val LOGIN_SCREEN = "Login"
const val REGISTER_SCREEN = "Register"

const val HOME_SCREEN = "Home"
const val INTERESTS_SCREEN = "Interests"

const val ADDFORM_SCREEN = "AddForm"

const val CREDITS_SCREEN = "Credits"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel : FirebaseViewModel, navController : NavHostController = rememberNavController()) {


    var showAppBar by remember { mutableStateOf(false) }
    var navigateAfterRegister by remember { mutableStateOf(false) }
    var navigateAfterLogin by remember { mutableStateOf(false) }


    navController.addOnDestinationChangedListener{ controller, destination, arguments ->
        //showAppBar = (destination.route != MENU_SCREEN)

        navigateAfterRegister = destination.route in arrayOf(
            REGISTER_SCREEN
        )
        navigateAfterLogin = destination.route in arrayOf(
            LOGIN_SCREEN
        )
    }

    Scaffold (
        topBar = {
            if(showAppBar)
                TopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    actions = {
                       /* if(showAddAction)
                            IconButton(onClick = {
                                navController.popBackStack()    // VOLTA PARA O MENU INICIAL
                                navController.navigate(DRAWING_SCREEN)
                            }) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add",
                                )
                            }

                        if(showDoneAction)
                            IconButton(onClick = {
                                navController.popBackStack()    // VOLTA PARA O MENU INICIAL
                                //navController.navigate(DRAWING_SCREEN)
                            }) {
                                Icon(
                                    Icons.Filled.Done,
                                    contentDescription = "Done",
                                )
                            }*/
                    },
                    colors = topAppBarColors(
                        containerColor = Color(0, 80, 150, 255),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = MaterialTheme.colorScheme.inversePrimary,
                    ),
                    modifier = Modifier
                        .height(50.dp)
                )
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = MENU_SCREEN,
            modifier = Modifier
                .padding(it)

        ) {
            composable("Menu") {
                Menu(
                    stringResource(R.string.nameApp),
                    navController,
                )
            }
            composable(LOGIN_SCREEN) {
                LoginScreen(viewModel, navController){
                    navController.navigate(HOME_SCREEN)
                }
            }
            composable(REGISTER_SCREEN) {
                RegisterScreen(viewModel, navController){
                    navController.navigate(HOME_SCREEN)
                }
            }
            composable(HOME_SCREEN) {
                HomeScreen(viewModel, navController){
                    navController.navigate("Menu")
                }
            }
            composable(INTERESTS_SCREEN) {
                InterestsScreen(viewModel,navController)
            }

            composable(ADDFORM_SCREEN){
                AddFormScreen(viewModel,navController)
            }

            composable(CREDITS_SCREEN){
                CreditsScreen(navController)
            }
        }
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    //MainScreen()
}