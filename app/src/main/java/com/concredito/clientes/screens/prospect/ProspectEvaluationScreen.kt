package com.concredito.clientes.screens.prospect

import FileItemForDownload
import LetterTile
import ObservationsDialog
import ProspectsAppBar
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.File
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.model.ProspectStatus
import com.concredito.clientes.model.RejectObservation
import com.concredito.clientes.ui.theme.Dimens.densityPixels4
import com.concredito.clientes.ui.theme.Dimens.densityPixels16
import com.concredito.clientes.ui.theme.Dimens.densityPixels8
import com.concredito.clientes.ui.theme.Dimens.densityPixels160
import com.concredito.clientes.ui.theme.Fonts.fontSizeHuge
import com.concredito.clientes.ui.theme.Fonts.fontSizeLarge
import com.concredito.clientes.ui.theme.Fonts.fontSizeMedium
import com.concredito.clientes.ui.theme.Fonts.fontSizeNormal
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.Constants.MESSAGE_URI
import com.concredito.clientes.util.Constants.ONE_LINE
import com.concredito.clientes.util.Constants.PHONE_URI
import com.concredito.clientes.util.formatSize
import com.concredito.clientes.util.getExtension
import com.concredito.clientes.util.getIconResource
import java.util.UUID

@Composable
fun ProspectEvaluationScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    prospectId: String,
    prospectsViewModel: ProspectsViewModel = hiltViewModel(),
    fileViewModel: FileViewModel = hiltViewModel(),
    rejectObservationViewModel: RejectObservationViewModel = hiltViewModel(),
) {
    val prospect = produceState<Resource<Prospect>>(initialValue = Resource.Loading()) {
        value = prospectsViewModel.getProspectById(prospectId)
    }.value

    val files = produceState<Resource<List<File>>>(initialValue = Resource.Loading()) {
        value = fileViewModel.getFilesByProspect(prospectId)
    }.value

    val observation =
        produceState<Resource<List<RejectObservation>>>(initialValue = Resource.Loading()) {
            value = rejectObservationViewModel.getRejectObservationsByProspectId(prospectId)
        }.value

    val rejectObservationId = remember { UUID.randomUUID().toString() }

    var observations by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(topBar = {
        ProspectsAppBar(
            title =
            if ((prospect.data?.status ?: ProspectStatus.ENVIADO) == ProspectStatus.ENVIADO) {
                stringArrayResource(id = R.array.prospect_evaluation_screen_app_bar_text)[0]
            } else {
                stringArrayResource(id = R.array.prospect_evaluation_screen_app_bar_text)[1]
            },
            isHome = false,
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
            navController = navController,
            onBackPressed = {
                navController.popBackStack()
            },
        )
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (prospect.data == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                    Text(text = stringResource(id = R.string.label_circular_progress_indicator_text))
                }
            } else {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(densityPixels16),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            tonalElevation = densityPixels8,
                            shadowElevation = densityPixels8,
                        ) {
                            LetterTile(
                                text = prospect.data.name.take(1),
                                size = densityPixels160,
                                fontSize = fontSizeHuge,
                                modifier = Modifier
                                    .align(
                                        Alignment.CenterHorizontally,
                                    )
                                    .padding(densityPixels4),
                            )
                        }

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "${prospect.data.name} ${prospect.data.surname}",
                            fontWeight = FontWeight.Black,
                            fontFamily = assistantFamily,
                            fontSize = fontSizeLarge,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = ONE_LINE,
                        )

                        if (prospect.data.secondSurname?.isNotEmpty() == true) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = prospect.data.secondSurname,
                                fontWeight = FontWeight.Black,
                                fontFamily = assistantFamily,
                                fontSize = fontSizeLarge,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = ONE_LINE,
                            )
                        } else {
                            Box {}
                        }

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = stringResource(id = R.string.label_rfc_text, prospect.data.rfc),
                            fontWeight = FontWeight.Medium,
                            fontFamily = assistantFamily,
                            fontSize = fontSizeMedium,
                            maxLines = ONE_LINE,
                        )

                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = densityPixels16),
                            shape = RoundedCornerShape(densityPixels4),
                            color =
                            when (prospect.data.status) {
                                ProspectStatus.ENVIADO -> MaterialTheme.colorScheme.surfaceVariant
                                ProspectStatus.AUTORIZADO -> MaterialTheme.colorScheme.primaryContainer
                                ProspectStatus.RECHAZADO -> MaterialTheme.colorScheme.errorContainer
                            },
                        ) {
                            Row(
                                modifier = Modifier.padding(densityPixels8),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = when (prospect.data.status) {
                                        ProspectStatus.ENVIADO -> Icons.AutoMirrored.Rounded.Send
                                        ProspectStatus.AUTORIZADO -> Icons.Rounded.CheckCircle
                                        ProspectStatus.RECHAZADO -> Icons.Rounded.Cancel
                                    },
                                    contentDescription = stringResource(id = R.string.icon_status_content_description),
                                )
                                Spacer(modifier = Modifier.padding(horizontal = densityPixels4))
                                prospect.data.status.let {
                                    Text(
                                        text = it.name,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = assistantFamily,
                                        fontSize = fontSizeNormal,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = ONE_LINE,
                                    )
                                }
                            }
                        }

                        if (prospect.data.status == ProspectStatus.ENVIADO) {
                            Row(modifier = Modifier.padding(horizontal = densityPixels16)) {
                                Button(
                                    modifier = Modifier.weight(1F),
                                    onClick = {
                                        prospectsViewModel.updateProspect(
                                            prospectId,
                                            Prospect(
                                                id = prospectId,
                                                promoterId = prospect.data.promoterId,
                                                name = prospect.data.name,
                                                surname = prospect.data.surname,
                                                secondSurname = prospect.data.secondSurname,
                                                streetAddress = prospect.data.streetAddress,
                                                numberAddress = prospect.data.numberAddress,
                                                neighborhood = prospect.data.neighborhood,
                                                zipCode = prospect.data.zipCode,
                                                phoneNumber = prospect.data.phoneNumber,
                                                rfc = prospect.data.rfc,
                                                status = ProspectStatus.AUTORIZADO,
                                            ),
                                        )

                                        navController.popBackStack()
                                    },
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription =
                                            stringResource(id = R.string.authorize_icon_content_description),
                                        )
                                        Spacer(modifier = Modifier.padding(horizontal = densityPixels4))
                                        Text(
                                            text = stringResource(id = R.string.button_authorize_prospect),
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.padding(horizontal = densityPixels4))
                                OutlinedButton(
                                    modifier = Modifier.weight(1F),
                                    onClick = {
                                        showDialog = true
                                    },
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Cancel,
                                            contentDescription =
                                            stringResource(id = R.string.reject_icon_content_description),
                                        )
                                        Spacer(modifier = Modifier.padding(horizontal = densityPixels4))
                                        Text(
                                            text = stringResource(id = R.string.button_reject_prospect),
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                }
                            }
                        } else {
                            Box {}
                        }

                        Column(modifier = Modifier.padding(densityPixels16)) {
                            Text(
                                text = stringArrayResource(id = R.array.labels_address_text)[0],
                                fontSize = fontSizeMedium,
                                fontWeight = FontWeight.Bold,
                                fontFamily = assistantFamily,
                            )
                            Row(
                                modifier = Modifier.padding(vertical = densityPixels8),
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription =
                                    stringResource(id = R.string.location_icon_content_description),
                                    tint = MaterialTheme.colorScheme.primary,
                                )

                                Spacer(modifier = Modifier.padding(horizontal = densityPixels4))
                                Column {
                                    Row {
                                        Text(
                                            text = stringArrayResource(id = R.array.labels_address_text)[1],
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                        Text(
                                            text = prospect.data.streetAddress,
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = stringArrayResource(id = R.array.labels_address_text)[2],
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                        Text(
                                            text = prospect.data.numberAddress,
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = stringArrayResource(id = R.array.labels_address_text)[3],
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                        Text(
                                            text = prospect.data.neighborhood,
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = stringArrayResource(id = R.array.labels_address_text)[4],
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                        Text(
                                            text = prospect.data.zipCode,
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                }
                            }
                        }
                        Column(modifier = Modifier.padding(densityPixels16)) {
                            Text(
                                text = stringArrayResource(id = R.array.labels_address_text)[5],
                                fontSize = fontSizeMedium,
                                fontWeight = FontWeight.Bold,
                                fontFamily = assistantFamily,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Phone,
                                    contentDescription = stringResource(id = R.string.phone_icon_content_description),
                                    tint = MaterialTheme.colorScheme.primary,
                                )

                                Spacer(modifier = Modifier.padding(horizontal = densityPixels4))
                                Text(
                                    text = stringArrayResource(id = R.array.labels_address_text)[6],
                                    fontSize = fontSizeNormal,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = assistantFamily,
                                )
                                Text(
                                    text = prospect.data.phoneNumber,
                                    fontSize = fontSizeNormal,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        val phoneURI =
                                            Uri.parse(PHONE_URI + (prospect.data.phoneNumber))
                                        val intentCall = Intent(Intent.ACTION_DIAL, phoneURI)
                                        try {
                                            context.startActivity(intentCall)
                                        } catch (s: SecurityException) {
                                            Toast.makeText(
                                                context,
                                                getString(
                                                    context,
                                                    R.string.message_launch_intent_error,
                                                ),
                                                Toast.LENGTH_LONG,
                                            )
                                                .show()
                                        }
                                    },
                                    colors = IconButtonDefaults.filledIconButtonColors(
                                        MaterialTheme.colorScheme.primary,
                                    ),
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Phone,
                                        contentDescription =
                                        stringResource(id = R.string.phone_icon_content_description),
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val messageURI =
                                            Uri.parse(MESSAGE_URI + (prospect.data.phoneNumber))
                                        val intentMessage = Intent(Intent.ACTION_VIEW, messageURI)
                                        try {
                                            context.startActivity(intentMessage)
                                        } catch (s: SecurityException) {
                                            Toast.makeText(
                                                context,
                                                getString(
                                                    context,
                                                    R.string.message_launch_intent_error,
                                                ),
                                                Toast.LENGTH_LONG,
                                            )
                                                .show()
                                        }
                                    },
                                    colors = IconButtonDefaults.filledIconButtonColors(
                                        MaterialTheme.colorScheme.primary,
                                    ),
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.Message,
                                        contentDescription =
                                        stringResource(id = R.string.message_icon_content_description),
                                    )
                                }
                            }
                        }
                        files.data?.let {
                            Column(modifier = Modifier.padding(densityPixels16)) {
                                Text(
                                    text = stringResource(id = R.string.label_attachments_text),
                                    fontSize = fontSizeMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = assistantFamily,
                                )
                                LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                                    items(items = it) {
                                        FileItemForDownload(
                                            icon = getIconResource(getExtension(it.fileType)),
                                            name = it.name,
                                            size = formatSize(it.fileSize.toLong())
                                        )
                                    }
                                }
                            }
                        }

                        if (prospect.data.status == ProspectStatus.RECHAZADO) {
                            Column(modifier = Modifier.padding(densityPixels16)) {
                                Text(
                                    text = stringResource(id = R.string.label_reject_observations_text),
                                    fontSize = fontSizeMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = assistantFamily,
                                )
                                Row {
                                    Icon(
                                        imageVector = Icons.Rounded.Visibility,
                                        contentDescription =
                                        stringResource(id = R.string.observations_icon_content_description),
                                        tint = MaterialTheme.colorScheme.primary,
                                    )

                                    Spacer(modifier = Modifier.padding(horizontal = densityPixels4))
                                    observation.data?.last()?.let {
                                        Text(
                                            text = it.observations,
                                            fontSize = fontSizeNormal,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                }
                            }
                        } else {
                            Box {}
                        }

                        if (showDialog) {
                            ObservationsDialog(
                                onDismiss = {
                                    showDialog = false
                                },
                                observations = observations,
                                onObservationsChange = { text ->
                                    observations = text
                                },
                                onClickSend = {
                                    prospectsViewModel.updateProspect(
                                        prospectId,
                                        Prospect(
                                            id = prospectId,
                                            promoterId = prospect.data.promoterId,
                                            name = prospect.data.name,
                                            surname = prospect.data.surname,
                                            secondSurname = prospect.data.secondSurname,
                                            streetAddress = prospect.data.streetAddress,
                                            numberAddress = prospect.data.numberAddress,
                                            neighborhood = prospect.data.neighborhood,
                                            zipCode = prospect.data.zipCode,
                                            phoneNumber = prospect.data.phoneNumber,
                                            rfc = prospect.data.rfc,
                                            status = ProspectStatus.RECHAZADO,
                                        ),
                                    )

                                    val rejectObservation = RejectObservation(
                                        id = rejectObservationId,
                                        observations = observations,
                                        prospectId = prospectId,
                                    )

                                    rejectObservationViewModel.addRejectObservations(
                                        rejectObservation,
                                    )
                                    navController.popBackStack()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
