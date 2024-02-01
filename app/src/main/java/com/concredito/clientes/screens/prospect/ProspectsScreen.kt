package com.concredito.clientes.screens.prospect

import LetterTile
import ProspectiveCustomerAppBar
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.model.ProspectStatus
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.ui.theme.assistantFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProspectsScreen(
    navController: NavHostController,
    promoterId: String,
    prospectViewModel: ProspectViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        ProspectiveCustomerAppBar(
            title = "Listado de prospectos",
            isHome = false,
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
            navController = navController,
            onBackPressed = { navController.navigate(AppScreens.MainScreen.name) },
        )
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ProspectsContent(navController = navController, promoterId, prospectViewModel)
        }
    }
}

@Composable
fun ProspectsContent(
    navController: NavHostController,
    promoterId: String,
    prospectViewModel: ProspectViewModel = hiltViewModel(),
) {
    var listOfPromoterId by remember { mutableStateOf<Resource<List<Prospect>>>(Resource.Loading()) }

    LaunchedEffect(promoterId) {
        listOfPromoterId = promoterId.let { prospectViewModel.getProspectsByPromoterId(it) }
    }

    Column {
        listOfPromoterId.data?.let { ProspectList(navController = navController, listOfProspects = it) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProspectList(
    navController: NavHostController,
    listOfProspects: List<Prospect>,
    prospectViewModel: ProspectViewModel = hiltViewModel(),
) {
    val sectionVisibility = remember { mutableStateMapOf<String, Boolean>() }
    if (prospectViewModel.isLoading) {
        Row(
            modifier = Modifier.padding(end = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    } else {
        LazyColumn {
            val groupedProspects = listOfProspects.groupBy { it.estatus }

            if (sectionVisibility.isEmpty()) {
                groupedProspects.keys.forEach { estatus ->
                    sectionVisibility[estatus.name] = true
                }
            }

            // Ordenar los grupos según tu preferencia
            val sortedGroups = groupedProspects.entries.sortedBy { entry ->
                when (entry.key) {
                    ProspectStatus.ENVIADO -> 0
                    ProspectStatus.AUTORIZADO -> 1
                    ProspectStatus.RECHAZADO -> 2
                    // Agrega más casos según tus estados
                    else -> 3
                }
            }

            sortedGroups.forEach { (estatus, prospects) ->
                val visible = sectionVisibility[estatus.name] == true

                stickyHeader {
                    TitleSection(
                        label = when (estatus) {
                            ProspectStatus.ENVIADO -> "ENVIADOS"
                            ProspectStatus.AUTORIZADO -> "AUTORIZADOS"
                            ProspectStatus.RECHAZADO -> "RECHAZADOS"
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
                navController.navigate(AppScreens.ProspectEvaluationScreen.name + "/${prospect.id}") {
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
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LetterTile(
                text = prospect.nombre.take(1),
                size = 48,
                fontSize = 22,
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "${prospect.nombre} ${prospect.primerApellido} ${prospect.segundoApellido}",
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = prospect.estatus.name,
                    fontWeight = FontWeight.Medium,
                    fontFamily = assistantFamily,
                    fontSize = 16.sp,
                    maxLines = 1,
                    color = when (prospect.estatus) {
                        ProspectStatus.ENVIADO -> MaterialTheme.colorScheme.onSurfaceVariant
                        ProspectStatus.AUTORIZADO -> MaterialTheme.colorScheme.primary
                        ProspectStatus.RECHAZADO -> MaterialTheme.colorScheme.error
                    },
                )
            }
        }
    }
}
