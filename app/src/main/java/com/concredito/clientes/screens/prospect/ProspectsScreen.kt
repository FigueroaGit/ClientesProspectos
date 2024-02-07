package com.concredito.clientes.screens.prospect

import LetterTile
import ProspectsAppBar
import TitleSection
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.model.ProspectStatus
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.ui.theme.Dimens.dimenNormal
import com.concredito.clientes.ui.theme.Dimens.dimenSmall
import com.concredito.clientes.ui.theme.Dimens.letterTileSize2x
import com.concredito.clientes.ui.theme.Fonts.fontSizeMedium
import com.concredito.clientes.ui.theme.Fonts.fontSizeNormal
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.Constants.FIRST
import com.concredito.clientes.util.Constants.LETTER_TILE_FONT_SIZE_2X
import com.concredito.clientes.util.Constants.ONE_LINE
import com.concredito.clientes.util.Constants.SECOND
import com.concredito.clientes.util.Constants.THIRD

@Composable
fun ProspectsScreen(
    navController: NavHostController,
    promoterId: String,
    prospectsViewModel: ProspectsViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        ProspectsAppBar(
            title = stringResource(id = R.string.prospects_screen_app_bar_text),
            isHome = false,
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
            navController = navController,
            onBackPressed = { navController.navigate(AppScreens.MainScreen.name) },
        )
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ProspectsContent(navController = navController, promoterId, prospectsViewModel)
        }
    }
}

@Composable
fun ProspectsContent(
    navController: NavHostController,
    promoterId: String,
    prospectsViewModel: ProspectsViewModel = hiltViewModel(),
) {
    var listOfPromoterId by remember { mutableStateOf<Resource<List<Prospect>>>(Resource.Loading()) }

    LaunchedEffect(promoterId) {
        listOfPromoterId = promoterId.let { promoterId ->
            prospectsViewModel.getProspectsByPromoterId(promoterId)
        }
    }

    Column {
        ProspectList(navController = navController, listOfProspects = listOfPromoterId)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProspectList(
    navController: NavHostController,
    listOfProspects: Resource<List<Prospect>>,
) {
    val sectionVisibility = remember { mutableStateMapOf<String, Boolean>() }

    when (listOfProspects) {
        is Resource.Success -> {
            if (listOfProspects.data?.isEmpty() == true) {
                Resource.Loading(data = listOfProspects)
            } else {
                LazyColumn {
                    val groupedProspects = listOfProspects.data?.groupBy { it.status }

                    if (sectionVisibility.isEmpty()) {
                        groupedProspects?.keys?.forEach { estatus ->
                            sectionVisibility[estatus.name] = true
                        }
                    }

                    // Sort the groups according to your preference
                    val sortedGroups = groupedProspects?.entries?.sortedBy { entry ->
                        when (entry.key) {
                            ProspectStatus.ENVIADO -> FIRST
                            ProspectStatus.AUTORIZADO -> SECOND
                            ProspectStatus.RECHAZADO -> THIRD
                        }
                    }

                    sortedGroups?.forEach { (estatus, prospects) ->
                        val visible = sectionVisibility[estatus.name] == true

                        stickyHeader {
                            TitleSection(
                                label = when (estatus) {
                                    ProspectStatus.ENVIADO ->
                                        stringResource(id = R.string.label_prospects_sent_text)
                                    ProspectStatus.AUTORIZADO ->
                                        stringResource(id = R.string.label_prospects_authorized_text)
                                    ProspectStatus.RECHAZADO ->
                                        stringResource(id = R.string.label_prospects_rejected_text)
                                },
                                onClickArrow = {
                                    sectionVisibility[estatus.name] =
                                        sectionVisibility[estatus.name]?.not() ?: true
                                },
                                isSectionVisible = visible,
                            )
                        }

                        items(items = prospects) { prospect ->
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut() + slideOutVertically(),
                            ) {
                                ProspectItem(prospect, navController)
                            }
                        }
                    }
                }
            }
        }

        is Resource.Loading -> {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        is Resource.Error -> {
            Text(
                text = "Error: ${listOfProspects.message}",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimenNormal),
            )
        }
    }
}

@Composable
fun ProspectItem(
    prospect: Prospect,
    navController: NavHostController,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier.clickable {
                navController.navigate(
                    AppScreens.ProspectEvaluationScreen.name + "/${prospect.id}",
                ) {
                    launchSingleTop = true
                }
            },
        ) {
            ProspectInfo(prospect = prospect)
        }
    }
}

@Composable
fun ProspectInfo(prospect: Prospect) {
    Column(modifier = Modifier.padding(horizontal = dimenSmall)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LetterTile(
                text = prospect.name.take(1),
                size = letterTileSize2x,
                fontSize = LETTER_TILE_FONT_SIZE_2X,
            )
            Column(modifier = Modifier.padding(dimenSmall)) {
                Text(
                    text = "${prospect.name} ${prospect.surname} ${prospect.secondSurname}",
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                    fontSize = fontSizeMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = ONE_LINE,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = prospect.status.name,
                    fontWeight = FontWeight.Medium,
                    fontFamily = assistantFamily,
                    fontSize = fontSizeNormal,
                    maxLines = ONE_LINE,
                    color = when (prospect.status) {
                        ProspectStatus.ENVIADO -> MaterialTheme.colorScheme.onSurfaceVariant
                        ProspectStatus.AUTORIZADO -> MaterialTheme.colorScheme.primary
                        ProspectStatus.RECHAZADO -> MaterialTheme.colorScheme.error
                    },
                )
            }
        }
    }
}
