package com.concredito.clientes.navigation

enum class AppScreens {
    SplashScreen,
    LoginScreen,
    MainScreen,
    NewProspectScreen,
    ProspectsScreen,
    ProspectEvaluationScreen,
    ;
    companion object {
        fun fromRoute(route: String?): AppScreens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            MainScreen.name -> MainScreen
            NewProspectScreen.name -> NewProspectScreen
            ProspectsScreen.name -> ProspectsScreen
            ProspectEvaluationScreen.name -> ProspectEvaluationScreen
            null -> LoginScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}
