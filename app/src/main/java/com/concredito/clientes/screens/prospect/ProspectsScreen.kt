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
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.model.ProspectStatus
import com.concredito.clientes.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ProspectsScreen(
    navController: NavHostController,
    prospectViewModel: ProspectViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        ProspectiveCustomerAppBar(
            title = "Prospects List",
            isHome = false,
            icon = Icons.Rounded.ArrowBack,
            navController = navController,
            onBackPressed = { navController.navigate(AppScreens.MainScreen.name) },
        )
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ProspectsContent(navController = navController, prospectViewModel)
        }
    }
}

@Composable
fun ProspectsContent(
    navController: NavHostController,
    prospectViewModel: ProspectViewModel = hiltViewModel(),
) {
    val listOfProspects = prospectViewModel.list
    Column {
        ProspectList(navController = navController, listOfProspects = listOfProspects)
        /*TitleSection(label = "Sent", onClickArrow = { })
        ProspectsSentArea(navController = navController, listOfProspects)
        TitleSection(label = "Authorized", onClickArrow = { })
        ProspectsAuthorizedArea(navController = navController, listOfProspects = listOfProspects)
        TitleSection(label = "Rejected", onClickArrow = { })
        ProspectsRejectedArea(navController = navController, listOfProspects = listOfProspects)*/
    }
}

/*@Composable
fun ProspectsSentArea(navController: NavHostController, listOfProspects: List<Prospect>) {
    val prospectSend =
        listOfProspects.filter { prospect -> prospect.estatus == ProspectStatus.ENVIADO }
    ProspectList(navController = navController, listOfProspects = prospectSend)
}

@Composable
fun ProspectsAuthorizedArea(navController: NavHostController, listOfProspects: List<Prospect>) {
    val prospectAuthorized =
        listOfProspects.filter { prospect -> prospect.estatus == ProspectStatus.AUTORIZADO }
    ProspectList(navController = navController, listOfProspects = prospectAuthorized)
}

@Composable
fun ProspectsRejectedArea(navController: NavHostController, listOfProspects: List<Prospect>) {
    val prospectRejected =
        listOfProspects.filter { prospect -> prospect.estatus == ProspectStatus.RECHAZADO }
    ProspectList(navController = navController, listOfProspects = prospectRejected)
}*/

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
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
        /*LazyColumn {
            items(items = listOfProspects) { prospect ->
                ProspectItem(prospect, navController)
            }
        }*/
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
                            ProspectStatus.ENVIADO -> "Sent"
                            ProspectStatus.AUTORIZADO -> "Authorized"
                            ProspectStatus.RECHAZADO -> "Rejected"
                            // Agrega más casos según tus estados
                            else -> "Other"
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
@Preview
fun ProspectItem(
    prospect: Prospect = Prospect(
        id = "1",
        idPromotor = "1",
        nombre = "Nombre",
        primerApellido = "Primer Apellido",
        segundoApellido = null,
        calle = "x",
        numero = "1",
        colonia = "x",
        codigoPostal = "00000",
        telefono = "5555555555",
        rfc = "XXXXXXXXXXXXXX",
        estatus = ProspectStatus.ENVIADO,
    ),
    navController: NavHostController = NavHostController(LocalContext.current),
) {
    /*Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Box(modifier = Modifier.clickable { navController.navigate(AppScreens.ProspectEvaluationScreen.name + "/${prospect.id}") }) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "${prospect.nombre} ${prospect.primerApellido} ${prospect.segundoApellido}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = prospect.estatus.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    color = when (prospect.estatus) {
                        ProspectStatus.ENVIADO -> Color.Gray
                        ProspectStatus.AUTORIZADO -> Color.Green
                        ProspectStatus.RECHAZADO -> Color.Red
                    },
                )
            }
        }
    }*/
    Surface(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier.clickable {
                navController.navigate(AppScreens.ProspectEvaluationScreen.name + "/${prospect.id}") {
                    launchSingleTop = true
                }
            },
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LetterTile(
                        text = "${prospect.nombre.take(1)}${prospect.primerApellido.take(1)}",
                        size = 48,
                        fontSize = 22,
                    )
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "${prospect.nombre} ${prospect.primerApellido} ${prospect.segundoApellido}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = prospect.estatus.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            color = when (prospect.estatus) {
                                ProspectStatus.ENVIADO -> Color.Gray
                                ProspectStatus.AUTORIZADO -> Color.Green
                                ProspectStatus.RECHAZADO -> Color.Red
                            },
                        )
                    }
                }
            }
        }
    }
}
