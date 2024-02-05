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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.screens.prospect.ProspectItem
import com.concredito.clientes.screens.prospect.ProspectsViewModel
import com.concredito.clientes.ui.theme.Dimens.circularIndicator
import com.concredito.clientes.ui.theme.Dimens.dimenNormal
import com.concredito.clientes.ui.theme.Dimens.dimenSmall
import com.concredito.clientes.ui.theme.Dimens.imageSizeSmall
import com.concredito.clientes.ui.theme.assistantFamily

@Composable
fun MainScreen(
    navController: NavHostController,
    prospectsViewModel: ProspectsViewModel = hiltViewModel(),
) {
    val promoterId = prospectsViewModel.getPromoterId()
    val username = prospectsViewModel.getUsername()
    val context = LocalContext.current

    var listOfPromoterId by remember { mutableStateOf<Resource<List<Prospect>>>(Resource.Loading()) }

    LaunchedEffect(promoterId) {
        listOfPromoterId = promoterId?.let { prospectsViewModel.getProspectsByPromoterId(it) }
            ?: Resource.Loading()
    }

    Scaffold(
        topBar = {
            ProspectsLargeTopAppBar(
                title = stringResource(id = R.string.welcome_user_text, "$username"),
                additionalText = stringResource(id = R.string.main_screen_app_bar_text),
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
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(circularIndicator)
                            .align(Alignment.Center),
                    )
                }

                is Resource.Error -> {
                    Text(
                        text = "Error: ${result.message}",
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimenNormal),
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
            .padding(dimenNormal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(imageSizeSmall),
            painter = painterResource(id = R.drawable.empty_list_placeholder),
            contentDescription = stringResource(id = R.string.empty_list_content_description),
            contentScale = ContentScale.Fit,
        )

        Text(
            modifier = Modifier.padding(vertical = dimenSmall),
            text = stringResource(id = R.string.empty_list_title),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = assistantFamily,
        )

        Text(
            text = stringResource(id = R.string.empty_list_paragraph),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontFamily = assistantFamily,
        )

        Button(
            modifier = Modifier.padding(dimenNormal),
            onClick = { navController.navigate(AppScreens.NewProspectScreen.name) },
        ) {
            Text(
                text = stringResource(id = R.string.button_add_prospect),
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
        Row(modifier = Modifier.padding(vertical = dimenSmall, horizontal = dimenNormal)) {
            Text(
                text = stringResource(id = R.string.label_last_prospect_added),
                fontWeight = FontWeight.Medium,
                fontFamily = assistantFamily,
            )
        }

        if (lastProspect != null) {
            ProspectItem(lastProspect, navController)
        }

        Row(
            modifier = Modifier.padding(vertical = dimenSmall, horizontal = dimenNormal)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.label_total_of_prospects_added),
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
                .padding(bottom = dimenNormal),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Button(
                modifier = Modifier.padding(dimenNormal),
                onClick = { navController.navigate(AppScreens.NewProspectScreen.name) },
            ) {
                Text(
                    text = stringResource(id = R.string.button_add_prospect),
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                )
            }
        }
    }
}
