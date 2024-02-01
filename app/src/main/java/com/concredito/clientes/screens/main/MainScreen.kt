package com.concredito.clientes.screens.main

import ProspectsLargeTopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.screens.prospect.ProspectItem
import com.concredito.clientes.screens.prospect.ProspectViewModel
import com.concredito.clientes.ui.theme.assistantFamily

@Composable
fun MainScreen(
    navController: NavHostController,
    prospectViewModel: ProspectViewModel = hiltViewModel(),
) {
    val promoterId = prospectViewModel.getPromoterId()
    val username = prospectViewModel.getUsername()
    val context = LocalContext.current

    var listOfPromoterId by remember { mutableStateOf<Resource<List<Prospect>>>(Resource.Loading()) }

    LaunchedEffect(promoterId) {
        listOfPromoterId = promoterId?.let { prospectViewModel.getProspectsByPromoterId(it) }
            ?: Resource.Error("PromoterId is null")
    }

    Scaffold(
        topBar = {
            ProspectsLargeTopAppBar(
                title = "Bienvenido, $username",
                additionalText = "Mi panel",
                navController = navController,
                onLogoutClicked = {
                    PreferencesManager(context).clearCredentials()
                    navController.navigate(AppScreens.LoginScreen.name) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                onMenuClicked = { /*TODO*/ },
                onShowProspectsClicked = { navController.navigate(AppScreens.ProspectsScreen.name + "/$promoterId") },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val result = listOfPromoterId) {
                is Resource.Success -> {
                    if (result.data?.isEmpty() == true) {
                        ShowEmptyListScreen(navController)
                    } else {
                        ShowProspectsListScreen(result, navController)
                    }
                }

                is Resource.Loading -> {
                    // Muestra un indicador de carga, si es necesario
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center),
                    )
                }

                is Resource.Error -> {
                    // Muestra un mensaje de error, puedes personalizarlo según tus necesidades
                    Text(
                        text = "Error: ${result.message}",
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun ShowEmptyListScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(256.dp),
            painter = painterResource(id = R.drawable.ic_list_empty_placeholder),
            contentDescription = "Imagen de lista vacia",
            contentScale = ContentScale.Fit, // Optional, adjust as needed
        )

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "¡Parece que no hay prospectos aquí!",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = assistantFamily,
        )

        Text(
            text = "Si deseas agregar prospectos y ver cuántos prospectos has agregado, comience a agregar prospectos con el botón a continuación",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontFamily = assistantFamily,
        )

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { navController.navigate(AppScreens.NewProspectScreen.name) },
        ) {
            Text(
                text = "Capturar prospecto",
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
            )
        }
    }
}

@Composable
fun ShowProspectsListScreen(
    listOfPromoterId: Resource<List<Prospect>>,
    navController: NavHostController,
) {
    val lastProspect = listOfPromoterId.data?.takeLast(1)?.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(
                text = "Ultimo prospecto agregado",
                fontWeight = FontWeight.Medium,
                fontFamily = assistantFamily,
            )
        }

        if (lastProspect != null) {
            ProspectItem(lastProspect, navController)
        }

        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Total de prospectos agregados",
                fontWeight = FontWeight.Medium,
                fontFamily = assistantFamily,
            )
            Text(
                text = "${listOfPromoterId.data?.size}",
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = { navController.navigate(AppScreens.NewProspectScreen.name) },
            ) {
                Text(
                    text = "Capturar prospecto",
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                )
            }
        }
    }
}
