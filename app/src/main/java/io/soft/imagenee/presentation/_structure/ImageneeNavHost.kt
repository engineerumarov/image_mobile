package io.soft.imagenee.presentation._structure

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.soft.imagenee.presentation.login.AuthorizeScreen
import io.soft.imagenee.presentation.main.MainScreen
import io.soft.imagenee.presentation.welcome.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageneeNavHost(navHostController: NavHostController) {
    val startDestination = "" // if (isUserSignedIn) main else welcome

    NavHost(
        navController = navHostController,
        startDestination = Destinations.main // start destination
    ) {
        composable(route = Destinations.welcome) {
            WelcomeScreen(
                onLogin = { navHostController.navigate(Destinations.login) },
                onSignUp = { navHostController.navigate(Destinations.signup) }
            )
        }

        composable(route = Destinations.login) {
            AuthorizeScreen(
                onBack = navHostController::popBackStack,
                isSignIn = true,
                onAuthorizeSuccess = {
                    navHostController.navigate(Destinations.main)
                }
            )
        }

        composable(route = Destinations.signup) {
            AuthorizeScreen(
                onBack = navHostController::popBackStack,
                isSignIn = false,
                onAuthorizeSuccess = {
                    navHostController.navigate(Destinations.main)
                }
            )
        }

        composable(route = Destinations.main) {
            MainScreen(
                onOpenImage = {
                    // navigate to SingleView screen
                }
            )
        }
    }
}