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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.screens.prospect.PromoterViewModel
import com.concredito.clientes.screens.prospect.ProspectList
import com.concredito.clientes.screens.prospect.ProspectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    promoterViewModel: PromoterViewModel = hiltViewModel(),
    prospectViewModel: ProspectViewModel = hiltViewModel(),
) {
    val promoterId = PreferencesManager(context = LocalContext.current).getPromoterId()
    val username = PreferencesManager(context = LocalContext.current).getUsername()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            /*ProspectiveCustomerAppBar(
                title = "Welcome, $username",
                navController = navController,
                onLogoutPressed = {
                    PreferencesManager(context).clearCredentials()
                    navController.navigate(AppScreens.LoginScreen.name) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
            )*/
            ProspectsLargeTopAppBar(
                title = "Welcome, $username",
                additionalText = "Dashboard",
                navController = navController,
                onLogoutClicked = {
                    PreferencesManager(context).clearCredentials()
                    navController.navigate(AppScreens.LoginScreen.name) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                onMenuClicked = { /*TODO*/ },
                onShowProspectsClicked = { navController.navigate(AppScreens.ProspectsScreen.name) },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            val listProspect = prospectViewModel.list
            val lastProspect = listProspect.takeLast(1)
            if (listProspect.isEmpty()) {
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
                        contentDescription = "List Empty Image",
                        contentScale = ContentScale.Fit, // Optional, adjust as needed
                    )

                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "It seems there's no prospects here!!",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = "If you want to add prospects and see how many prospects you've added start to adding prospects with the button below",
                        textAlign = TextAlign.Center,
                    )

                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = { navController.navigate(AppScreens.NewProspectScreen.name) },
                    ) {
                        Text(text = "Add prospect")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                        Text(text = "Last prospect added by you", fontWeight = FontWeight.Medium)
                    }

                    ProspectList(navController = navController, listOfProspects = lastProspect)

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Total prospects added by you ", fontWeight = FontWeight.Medium)
                        Text(text = "${listProspect.size}", fontWeight = FontWeight.Bold)
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
                            Text(text = "Add prospect")
                        }
                    }
                }
            }
        }
    }
}
