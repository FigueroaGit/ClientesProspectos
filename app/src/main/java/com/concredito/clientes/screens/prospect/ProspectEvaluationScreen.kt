package com.concredito.clientes.screens.prospect

import ProspectiveCustomerAppBar
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.model.ProspectStatus
import com.concredito.clientes.model.RejectObservation
import com.concredito.clientes.navigation.AppScreens
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ProspectEvaluationScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    prospectId: String,
    prospectViewModel: ProspectViewModel = hiltViewModel(),
    rejectObservationViewModel: RejectObservationViewModel = hiltViewModel(),
) {
    val prospect = produceState<Resource<Prospect>>(initialValue = Resource.Loading()) {
        value = prospectViewModel.getProspectById(prospectId)
    }.value

    val observation =
        produceState<Resource<List<RejectObservation>>>(initialValue = Resource.Loading()) {
            value = rejectObservationViewModel.getRejectObservationsByProspectId(prospectId)
        }.value

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        ProspectiveCustomerAppBar(
            title = "Prospect Evaluation",
            isHome = false,
            icon = Icons.Rounded.ArrowBack,
            navController = navController,
            onBackPressed = {
                navController.navigate(AppScreens.ProspectsScreen.name) {
                    launchSingleTop = true
                }
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
                    Text(text = "Loading...")
                }
            } else {
                Column {
                    ProspectHeader(prospect)
                    ProspectPersonalInformation(prospect)
                    ProspectAddressInformation(prospect)
                    ProspectDocuments()

                    when (prospect.data.estatus) {
                        ProspectStatus.ENVIADO -> AuthorizeRejectButtons(
                            onClickAuthorizeButton = {
                                prospectViewModel.updateProspect(
                                    prospectId,
                                    Prospect(
                                        id = prospectId,
                                        idPromotor = prospect.data.idPromotor,
                                        nombre = prospect.data.nombre,
                                        primerApellido = prospect.data.primerApellido,
                                        segundoApellido = prospect.data.segundoApellido,
                                        calle = prospect.data.calle,
                                        numero = prospect.data.numero,
                                        colonia = prospect.data.colonia,
                                        codigoPostal = prospect.data.codigoPostal,
                                        telefono = prospect.data.telefono,
                                        rfc = prospect.data.rfc,
                                        estatus = ProspectStatus.AUTORIZADO,
                                    ),
                                )

                                navController.navigate(AppScreens.ProspectsScreen.name)
                            },
                            onClickRejectButton = {
                                showDialog = true
                            },
                        )

                        ProspectStatus.RECHAZADO -> RejectionNotes(observation)
                        ProspectStatus.AUTORIZADO -> Box {}
                    }

                    if (showDialog) {
                        ObservationsDialog(
                            onDismiss = {
                                showDialog = false
                            },
                            onClickSend = {
                                prospectViewModel.updateProspect(
                                    prospectId,
                                    Prospect(
                                        id = prospectId,
                                        idPromotor = prospect.data.idPromotor,
                                        nombre = prospect.data.nombre,
                                        primerApellido = prospect.data.primerApellido,
                                        segundoApellido = prospect.data.segundoApellido,
                                        calle = prospect.data.calle,
                                        numero = prospect.data.numero,
                                        colonia = prospect.data.colonia,
                                        codigoPostal = prospect.data.codigoPostal,
                                        telefono = prospect.data.telefono,
                                        rfc = prospect.data.rfc,
                                        estatus = ProspectStatus.RECHAZADO,
                                    ),
                                )
                            },
                            prospectId,
                            rejectObservationViewModel,
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ProspectHeader(
    prospect: Resource<Prospect>,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${prospect.data?.nombre} ${prospect.data?.primerApellido}",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = "${prospect.data?.segundoApellido}",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = "RFC: ${prospect.data?.rfc}",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color =
                when (prospect.data?.estatus) {
                    ProspectStatus.ENVIADO -> Color.LightGray
                    ProspectStatus.AUTORIZADO -> Color.Green
                    ProspectStatus.RECHAZADO -> Color.Red
                    else -> {
                        Color.LightGray
                    }
                },
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = when (prospect.data?.estatus) {
                            ProspectStatus.ENVIADO -> Icons.Rounded.Send
                            ProspectStatus.AUTORIZADO -> Icons.Rounded.CheckCircle
                            ProspectStatus.RECHAZADO -> Icons.Rounded.Cancel
                            else -> {
                                Icons.Rounded.QuestionMark
                            }
                        },
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    prospect.data?.estatus?.let {
                        Text(
                            text = it.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ProspectPersonalInformation(
    prospect: Resource<Prospect>,
) {
    val ctx = LocalContext.current
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Contact Info",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
            )
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Rounded.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    modifier = Modifier.clickable {
                        val u = Uri.parse("tel:" + (prospect.data?.telefono ?: ""))
                        val i = Intent(Intent.ACTION_DIAL, u)
                        try {
                            ctx.startActivity(i)
                        } catch (s: SecurityException) {
                            Toast.makeText(ctx, "An error occurred", Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                    text = "${prospect.data?.telefono}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
@Preview
fun ProspectAddressInformation(
    prospect: Resource<Prospect>,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Address Info",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
            )
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = "${prospect.data?.calle}, ${prospect.data?.numero}, ${prospect.data?.colonia}, ${prospect.data?.codigoPostal}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
@Preview
fun ProspectDocuments() {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Documents Added",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
            )
            LazyRow {
                item {
                    DocumentItem()
                    DocumentItem(R.drawable.ic_png_file, "Credencial_prospecto_2024.png")
                    DocumentItem()
                }
            }
        }
    }
}

@Composable
@Preview
fun DocumentItem(
    iconType: Int = R.drawable.ic_pdf_file,
    name: String = "Formato_prospecto_2024.pdf",
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Box(modifier = Modifier.clickable { }) {
            Row(modifier = Modifier.padding(8.dp)) {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = iconType),
                    contentDescription = null,
                )
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .height(48.dp)
                        .width(1.dp),
                )
                val fileName = name.substringBeforeLast(".")
                val fileExtension = name.substringAfterLast(".")

                val truncatedFileName =
                    if (fileName.length > 15) "${fileName.substring(0, 15)}..." else fileName

                val truncatedText = "$truncatedFileName.$fileExtension"

                Text(
                    text = truncatedText,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
@Preview
fun RejectionNotes(rejectObservations: Resource<List<RejectObservation>>) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Rejection notes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1,
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (!expanded) Icons.Rounded.ExpandMore else Icons.Rounded.ExpandLess,
                        contentDescription = null,
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Box(modifier = Modifier.padding(bottom = 16.dp)) {
                    rejectObservations.data?.last()?.let { Text(text = it.observaciones) }
                }
            }
        }
    }
}

@Composable
@Preview
fun AuthorizeRejectButtons(onClickAuthorizeButton: () -> Unit, onClickRejectButton: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        FilledTonalButton(
            modifier = Modifier.weight(1F),
            onClick = { onClickAuthorizeButton.invoke() },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(text = "Authorize")
            }
        }
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        FilledTonalButton(
            modifier = Modifier.weight(1F),
            onClick = { onClickRejectButton.invoke() },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(imageVector = Icons.Rounded.Cancel, contentDescription = null)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(text = "Reject")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ObservationsDialog(
    onDismiss: () -> Unit,
    onClickSend: () -> Unit,
    prospectId: String,
    rejectObservationViewModel: RejectObservationViewModel = hiltViewModel(),
) {
    val rejectObservationId = remember { UUID.randomUUID().toString() }
    var observations by remember { mutableStateOf("") }
    val maxCharacters = 150

    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    IconButton(
                        onClick = {
                            // Manejar el clic en el botón de cerrar
                            onDismiss.invoke()
                            // ...
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                    Text("Ingrese las observaciones de rechazo")
                    Spacer(modifier = Modifier.size(48.dp)) // Espaciado para el icono
                }

                // Multiline TextField with character limit
                TextField(
                    value = observations,
                    onValueChange = {
                        // Limit the characters to maxCharacters
                        if (it.length <= maxCharacters) {
                            observations = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    label = { Text("Observaciones (Máx. $maxCharacters caracteres)") },
                    supportingText = {
                        Text(
                            text = "Caracteres restantes: ${maxCharacters - observations.length}",
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    },
                    singleLine = false,
                    maxLines = 5, // Ajustar según sea necesario
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Manejar la entrada de observaciones, por ejemplo, puedes imprimirla
                            println("Observations: $observations")

                            // Ocultar el teclado
                            keyboardController?.hide()

                            // Cerrar el diálogo
                            onDismiss.invoke()
                        },
                    ),
                )

                // Botón de enviar
                Button(
                    onClick = {
                        // Manejar la acción de enviar, por ejemplo, puedes imprimir las observaciones
                        println("Observations: $observations")

                        val rejectObservation = RejectObservation(
                            id = rejectObservationId,
                            observaciones = observations,
                            prospectoId = prospectId,
                        )

                        rejectObservationViewModel.addRejectObservations(rejectObservation)

                        // Cerrar el diálogo
                        onClickSend.invoke()
                        onDismiss.invoke()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
                    Icon(imageVector = Icons.Outlined.Send, contentDescription = "Enviar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar")
                }
            }
        }
    }
}
