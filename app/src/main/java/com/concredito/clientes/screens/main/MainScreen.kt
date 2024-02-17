package com.concredito.clientes.screens.main

import ActionTonalButton
import ProspectsLargeTopAppBar
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.screens.prospect.ProspectItem
import com.concredito.clientes.screens.prospect.ProspectsViewModel
import com.concredito.clientes.ui.theme.Dimens.densityPixels128
import com.concredito.clientes.ui.theme.Dimens.densityPixels64
import com.concredito.clientes.ui.theme.Dimens.densityPixels16
import com.concredito.clientes.ui.theme.Dimens.densityPixels24
import com.concredito.clientes.ui.theme.Dimens.densityPixels8
import com.concredito.clientes.ui.theme.Dimens.densityPixels256
import com.concredito.clientes.ui.theme.Dimens.densityPixels32
import com.concredito.clientes.ui.theme.Dimens.densityPixels4
import com.concredito.clientes.ui.theme.Dimens.densityPixels40
import com.concredito.clientes.ui.theme.Dimens.densityPixels48
import com.concredito.clientes.ui.theme.Dimens.densityPixels72
import com.concredito.clientes.ui.theme.Dimens.densityPixels80
import com.concredito.clientes.ui.theme.Fonts.fontSizeExtraSmall
import com.concredito.clientes.ui.theme.Fonts.fontSizeMedium
import com.concredito.clientes.ui.theme.Fonts.fontSizeNormal
import com.concredito.clientes.ui.theme.Fonts.fontSizeSmall
import com.concredito.clientes.ui.theme.assistantFamily
import kotlinx.coroutines.launch

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
                jobRole = stringResource(id = R.string.role_user_text)
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val result = listOfPromoterId) {
                is Resource.Success -> {
                    if (result.data?.isEmpty() == true) {
                        ShowEmptyListScreen(context, navController)
                    } else {
                        if (promoterId != null) {
                            ShowProspectsListScreen(context, promoterId, result, navController)
                        }
                    }
                }

                is Resource.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(densityPixels64),
                        )
                        Text(
                            text = stringResource(id = R.string.label_circular_progress_indicator_text),
                            fontSize = fontSizeNormal,
                            fontFamily = assistantFamily,
                        )
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = "${result.message}",
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(densityPixels16),
                    )
                }
            }
        }
    }
}

@Composable
fun ShowEmptyListScreen(context: Context, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(densityPixels16),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(densityPixels256),
            painter = painterResource(id = R.drawable.empty_list_placeholder),
            contentDescription = stringResource(id = R.string.empty_list_content_description),
            contentScale = ContentScale.Fit,
        )

        Text(
            modifier = Modifier.padding(vertical = densityPixels8),
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
            modifier = Modifier.padding(densityPixels16),
            onClick = { navController.navigate(AppScreens.NewProspectScreen.name) },
        ) {
            Text(
                text = stringResource(id = R.string.button_add_prospect_empty_list),
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = {
                PreferencesManager(context).clearCredentials()
                navController.navigate(AppScreens.LoginScreen.name) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .padding(densityPixels16)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.logout_content_description),
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = stringResource(id = R.string.logout_content_description),
                    modifier = Modifier.padding(start = densityPixels8)
                )
            }
        }
    }
}

@Composable
fun ShowProspectsListScreen(
    context: Context,
    promoterId: String,
    listOfPromoterId: Resource<List<Prospect>>,
    navController: NavHostController,
) {
    val lastProspect = listOfPromoterId.data?.takeLast(1)?.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(modifier = Modifier.padding(vertical = densityPixels8, horizontal = densityPixels16)) {
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
            modifier = Modifier
                .padding(vertical = densityPixels8, horizontal = densityPixels16)
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

        Row(
            modifier = Modifier
                .padding(horizontal = densityPixels16)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.label_dashboard_actions),
                fontSize = fontSizeMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily
            )
        }

        Row {
            ActionTonalButton(
                onClick = {
                    navController.navigate(AppScreens.NewProspectScreen.name) {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(
                        top = densityPixels16,
                        bottom = densityPixels16,
                        start = densityPixels24,
                        end = densityPixels8
                    ),
                icon = R.drawable.ic_add_info,
                text = stringResource(id = R.string.button_add_prospect),
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
                fontSize = fontSizeExtraSmall,
                textColor = MaterialTheme.colorScheme.onSurface
            )

            ActionTonalButton(
                onClick = {
                    navController.navigate(AppScreens.ProspectsScreen.name + "/$promoterId") {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(
                        top = densityPixels16,
                        bottom = densityPixels16,
                        start = densityPixels24,
                        end = densityPixels8
                    ),
                icon = R.drawable.ic_show_data,
                text = stringResource(id = R.string.button_show_all_prospects),
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
                fontSize = fontSizeExtraSmall,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = {
                PreferencesManager(context).clearCredentials()
                navController.navigate(AppScreens.LoginScreen.name) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .padding(densityPixels16)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.logout_content_description),
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = stringResource(id = R.string.logout_content_description),
                    modifier = Modifier.padding(start = densityPixels8)
                )
            }
        }
    }
}
