package pt.isec.a2020136093.amov

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isec.a2020136093.amov.guiaturistico.HomeScreen
import pt.isec.a2020136093.amov.guiaturistico.LoginScreen
import pt.isec.a2020136093.amov.guiaturistico.Menu
import pt.isec.a2020136093.amov.guiaturistico.R

const val MENU_SCREEN = "Menu"
const val LOGIN_SCREEN = "Login"
const val REGISTER_SCREEN = "Register"

const val HOME_SCREEN = "Home"


@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {

    var navigateAfterRegister by remember { mutableStateOf(false) }
    var navigateAfterLogin by remember { mutableStateOf(false) }


    navController.addOnDestinationChangedListener{ controller, destination, arguments ->
        navigateAfterRegister = destination.route in arrayOf(
            REGISTER_SCREEN
        )
        navigateAfterLogin = destination.route in arrayOf(
            LOGIN_SCREEN
        )
    }

    Scaffold (
        /*topBar = {
            if(showAppBar)
                TopAppBar(
                    title = { Text(text = stringResource()) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    actions = {
                        if(showAddAction)
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
                            }
                    },
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = MaterialTheme.colorScheme.inversePrimary,
                    ),
                )
        },*/
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = MENU_SCREEN,
            modifier = Modifier
                .padding(it)

        ) {
            composable("Menu") {
                Menu(
                    stringResource(R.string.welcome),
                    navController,
                    LOGIN_SCREEN,
                    REGISTER_SCREEN
                )
            }
            composable(LOGIN_SCREEN) {
                LoginScreen(navController)
            }
            composable(HOME_SCREEN) {
                HomeScreen()
            }

            composable(REGISTER_SCREEN) {
                //RegisterScreen()
            }
        }
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}