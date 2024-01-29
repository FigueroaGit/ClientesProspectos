package com.concredito.clientes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.concredito.clientes.screens.SplashScreen
import com.concredito.clientes.screens.login.LoginScreen
import com.concredito.clientes.screens.main.MainScreen
import com.concredito.clientes.screens.prospect.NewProspectScreen
import com.concredito.clientes.screens.prospect.ProspectDetailScreen
import com.concredito.clientes.screens.prospect.ProspectEvaluationScreen
import com.concredito.clientes.screens.prospect.ProspectsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.name) {
        composable(route = AppScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = AppScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(route = AppScreens.MainScreen.name) {
            MainScreen(navController = navController)
        }
        composable(route = AppScreens.NewProspectScreen.name) {
            NewProspectScreen(navController = navController)
        }
        composable(route = AppScreens.ProspectsScreen.name) {
            ProspectsScreen(navController = navController)
        }
        composable(route = AppScreens.ProspectDetailScreen.name) {
            ProspectDetailScreen(navController = navController)
        }
        composable(
            route = AppScreens.ProspectEvaluationScreen.name + "/{prospectId}",
            arguments = listOf(
                navArgument("prospectId") {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("prospectId").let {
                ProspectEvaluationScreen(navController = navController, prospectId = it.toString())
            }
        }
    }
}
