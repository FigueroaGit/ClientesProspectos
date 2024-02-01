package com.concredito.clientes.screens.prospect

import LetterTile
import ProspectiveCustomerAppBar
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.model.ProspectStatus
import com.concredito.clientes.model.RejectObservation
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.ui.theme.assistantFamily
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

    val context = LocalContext.current

    Scaffold(topBar = {
        ProspectiveCustomerAppBar(
            title = if ((
                    prospect.data?.estatus
                        ?: ProspectStatus.ENVIADO
                    ) == ProspectStatus.ENVIADO
            ) {
                "Evaluación del prospecto"
            } else {
                "Información del prospecto"
            },
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
                    Text(text = "Cargando...")
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
                                .padding(16.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            tonalElevation = 8.dp,
                            shadowElevation = 8.dp,
                        ) {
                            LetterTile(
                                text = prospect.data.nombre.take(1),
                                size = 160,
                                fontSize = 64,
                                modifier = Modifier
                                    .align(
                                        Alignment.CenterHorizontally,
                                    )
                                    .padding(4.dp),
                            )
                        }

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "${prospect.data.nombre} ${prospect.data.primerApellido}",
                            fontWeight = FontWeight.Black,
                            fontFamily = assistantFamily,
                            fontSize = 24.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )

                        if (prospect.data.segundoApellido != null) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = prospect.data.segundoApellido,
                                fontWeight = FontWeight.Black,
                                fontFamily = assistantFamily,
                                fontSize = 24.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "RFC: ${prospect.data.rfc}",
                            fontWeight = FontWeight.Medium,
                            fontFamily = assistantFamily,
                            fontSize = 20.sp,
                            maxLines = 1,
                        )

                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 16.dp),
                            shape = RoundedCornerShape(4.dp),
                            color =
                            when (prospect.data.estatus) {
                                ProspectStatus.ENVIADO -> MaterialTheme.colorScheme.surfaceVariant
                                ProspectStatus.AUTORIZADO -> MaterialTheme.colorScheme.primaryContainer
                                ProspectStatus.RECHAZADO -> MaterialTheme.colorScheme.errorContainer
                            },
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = when (prospect.data.estatus) {
                                        ProspectStatus.ENVIADO -> Icons.Rounded.Send
                                        ProspectStatus.AUTORIZADO -> Icons.Rounded.CheckCircle
                                        ProspectStatus.RECHAZADO -> Icons.Rounded.Cancel
                                    },
                                    contentDescription = null,
                                )
                                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                prospect.data.estatus.let {
                                    Text(
                                        text = it.name,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = assistantFamily,
                                        fontSize = 16.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                    )
                                }
                            }
                        }

                        if (prospect.data.estatus == ProspectStatus.ENVIADO) {
                            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Button(
                                    modifier = Modifier.weight(1F),
                                    onClick = {
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
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription = null,
                                        )
                                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                        Text(
                                            text = "Autorizar",
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
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
                                            contentDescription = null,
                                        )
                                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                        Text(
                                            text = "Rechazar",
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                }
                            }
                        } else {
                            Box {}
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Dirección:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = assistantFamily,
                            )
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )

                                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                Column {
                                    Row {
                                        Text(
                                            text = "Calle: ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                        Text(
                                            text = prospect.data.calle,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = "Número: ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                        Text(
                                            text = prospect.data.numero,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = "Colonia: ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                        Text(
                                            text = prospect.data.colonia,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = "Código postal: ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = assistantFamily,
                                        )
                                        Text(
                                            text = prospect.data.codigoPostal,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                        )
                                    }
                                }
                            }
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Teléfono:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = assistantFamily,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Phone,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )

                                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                Text(
                                    text = "Principal: ",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = assistantFamily,
                                )
                                Text(
                                    text = prospect.data.telefono,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        val u = Uri.parse("tel:" + (prospect.data.telefono))
                                        val i = Intent(Intent.ACTION_DIAL, u)
                                        try {
                                            context.startActivity(i)
                                        } catch (s: SecurityException) {
                                            Toast.makeText(
                                                context,
                                                "No se puede realizar esta accion",
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
                                        contentDescription = "Call",
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val u = Uri.parse("sms:" + (prospect.data.telefono))
                                        val i = Intent(Intent.ACTION_VIEW, u)
                                        try {
                                            context.startActivity(i)
                                        } catch (s: SecurityException) {
                                            Toast.makeText(
                                                context,
                                                "No se puede realizar esta accion",
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
                                        imageVector = Icons.Rounded.Message,
                                        contentDescription = "Message",
                                    )
                                }
                            }
                        }

                        if (prospect.data.estatus == ProspectStatus.RECHAZADO) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Observaciones:",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = assistantFamily,
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Visibility,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )

                                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                    observation.data?.last()?.let {
                                        Text(
                                            text = it.observaciones,
                                            fontSize = 16.sp,
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
}

/*
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
                fontFamily = assistantFamily,
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
                    fontWeight = FontWeight.Normal,
                    fontFamily = assistantFamily,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
*/

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
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
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
                        .heightIn(min = 154.dp),
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
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                )

                // Botón de enviar
                Button(
                    onClick = {
                        val rejectObservation = RejectObservation(
                            id = rejectObservationId,
                            observaciones = observations,
                            prospectoId = prospectId,
                        )

                        rejectObservationViewModel.addRejectObservations(rejectObservation)
                        onClickSend.invoke()
                        onDismiss.invoke()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Enviar",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar observaciones de rechazo")
                }
            }
        }
    }
}
