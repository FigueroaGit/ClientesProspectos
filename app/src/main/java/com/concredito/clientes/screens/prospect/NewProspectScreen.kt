package com.concredito.clientes.screens.prospect

import ExitDialog
import FormInputText
import ProspectiveCustomerAppBar
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.model.ProspectStatus
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.Constants.MAX_CHARACTERS_BY_ADDRESS
import com.concredito.clientes.util.Constants.MAX_CHARACTERS_BY_NAME
import com.concredito.clientes.util.Constants.MAX_CHARACTERS_BY_NUMBER_ADDRESS
import com.concredito.clientes.util.Constants.MAX_CHARACTERS_BY_PHONE_NUMBER
import com.concredito.clientes.util.Constants.MAX_CHARACTERS_BY_RFC
import com.concredito.clientes.util.Constants.MAX_CHARACTERS_BY_ZIP_CODE
import com.concredito.clientes.util.filterAddressInput
import com.concredito.clientes.util.filterLettersAndNumbers
import com.concredito.clientes.util.filterNameInput
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun NewProspectScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    prospectViewModel: ProspectViewModel = hiltViewModel(),
    documentViewModel: DocumentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val prospectId = remember { UUID.randomUUID().toString() }
    val promoterId = PreferencesManager(context).getPromoterId()
    var prospectName by remember { mutableStateOf("") }
    var prospectSurname by remember { mutableStateOf("") }
    var prospectSecondSurname by remember { mutableStateOf("") }
    var prospectStreetAddress by remember { mutableStateOf("") }
    var prospectNumberAddress by remember { mutableStateOf("") }
    var prospectNeighborhoodAddress by remember { mutableStateOf("") }
    var prospectZipCode by remember { mutableStateOf("") }
    var prospectPhoneNumber by remember { mutableStateOf("") }
    var prospectRFC by remember { mutableStateOf("") }

    var prospectNameSupportingText by remember { mutableStateOf<String?>(null) }
    var prospectSurnameSupportingText by remember { mutableStateOf<String?>(null) }
    var prospectStreetAddressSupportingText by remember { mutableStateOf<String?>(null) }
    var prospectNumberAddressSupportingText by remember { mutableStateOf<String?>(null) }
    var prospectNeighborhoodAddressSupportingText by remember { mutableStateOf<String?>(null) }
    var prospectZipCodeSupportingText by remember { mutableStateOf<String?>(null) }
    var prospectPhoneNumberSupportingText by remember { mutableStateOf<String?>(null) }
    var prospectRFCSupportingText by remember { mutableStateOf<String?>(null) }

    var showProspectNameError by remember { mutableStateOf(false) }
    var showProspectSurnameError by remember { mutableStateOf(false) }
    var showProspectStreetAddressError by remember { mutableStateOf(false) }
    var showProspectNumberAddressError by remember { mutableStateOf(false) }
    var showProspectNeighborhoodAddressError by remember { mutableStateOf(false) }
    var showProspectZipCodeError by remember { mutableStateOf(false) }
    var showProspectPhoneNumberError by remember { mutableStateOf(false) }
    var showProspectRFCError by remember { mutableStateOf(false) }

    /*var documentName by remember { mutableStateOf("") }
    var documentNameSupportingText by remember { mutableStateOf<String?>(null) }
    var showDocumentNameError by remember { mutableStateOf(false) }*/

    var documentUri by remember { mutableStateOf<Uri?>(null) }

    /* val pickDocument =
         rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
             // Handle the selected document URI here
             // You might want to store the URI in your ViewModel or perform additional operations
             documentUri = uri
         }*/

    var showExitDialog by remember { mutableStateOf(false) }
    /*var showDocumentSection by remember { mutableStateOf(false) }*/

    if (showExitDialog) {
        ExitDialog(
            onConfirm = {
                // Perform exit action
                navController.navigate(AppScreens.MainScreen.name)
            },
            onDismiss = {
                // Dismiss dialog
                showExitDialog = false
            },
        )
    }

    Scaffold(topBar = {
        ProspectiveCustomerAppBar(
            title = "New Customer",
            isHome = false,
            icon = Icons.Rounded.ArrowBack,
            navController = navController,
            onBackPressed = {
                if (
                    prospectName.isEmpty() &&
                    prospectSurname.isEmpty() &&
                    prospectSecondSurname.isEmpty() &&
                    prospectStreetAddress.isEmpty() &&
                    prospectNumberAddress.isEmpty() &&
                    prospectNeighborhoodAddress.isEmpty() &&
                    prospectZipCode.isEmpty() &&
                    prospectPhoneNumber.isEmpty() &&
                    prospectRFC.isEmpty()
                ) {
                    // Si todos los campos están vacíos, navegar directamente a MainScreen
                    navController.navigate(AppScreens.MainScreen.name)
                } else {
                    // Mostrar ExitDialog
                    showExitDialog = true
                }
            },
        )
    }) { paddingValues ->
        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                ) {
                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        text = prospectName,
                        label = "Customer Name",
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_NAME) {
                                prospectName = filterNameInput(it)
                            }
                            prospectNameSupportingText = null
                            showProspectNameError = false
                        },
                        supportingText = {
                            prospectNameSupportingText?.let {
                                Text(
                                    text = (it),
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                            }
                        },
                        isError = showProspectNameError,
                        keyboardType = KeyboardType.Text,
                        keyboardCapitalization = KeyboardCapitalization.Words,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = prospectSurname,
                        label = "Customer Surname",
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_NAME) {
                                prospectSurname = filterNameInput(it)
                            }
                            prospectSurnameSupportingText = null
                            showProspectSurnameError = false
                        },
                        supportingText = {
                            prospectSurnameSupportingText?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                            }
                        },
                        isError = showProspectSurnameError,
                        keyboardType = KeyboardType.Text,
                        keyboardCapitalization = KeyboardCapitalization.Words,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = prospectSecondSurname,
                        label = "Customer Second Surname",
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_NAME) {
                                prospectSecondSurname = filterNameInput(it)
                            }
                        },
                        keyboardType = KeyboardType.Text,
                        keyboardCapitalization = KeyboardCapitalization.Words,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = prospectStreetAddress,
                        label = "Street Address",
                        leadingIcon = Icons.Default.Home,
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_ADDRESS) {
                                prospectStreetAddress = filterAddressInput(it)
                            }
                            prospectStreetAddressSupportingText = null
                            showProspectStreetAddressError = false
                        },
                        supportingText = {
                            prospectStreetAddressSupportingText?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                            }
                        },
                        isError = showProspectStreetAddressError,
                        keyboardType = KeyboardType.Text,
                        keyboardCapitalization = KeyboardCapitalization.Words,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = prospectNumberAddress,
                        label = "Number Address",
                        leadingIcon = Icons.Default.Numbers,
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_NUMBER_ADDRESS) {
                                prospectNumberAddress = filterLettersAndNumbers(it)
                            }
                            prospectNumberAddressSupportingText = null
                            showProspectNumberAddressError = false
                        },
                        supportingText = {
                            prospectNumberAddressSupportingText?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                            }
                        },
                        isError = showProspectNumberAddressError,
                        keyboardType = KeyboardType.Text,
                        keyboardCapitalization = KeyboardCapitalization.Characters,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = prospectNeighborhoodAddress,
                        label = "Neighborhood Address",
                        leadingIcon = Icons.Default.Home,
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_ADDRESS) {
                                prospectNeighborhoodAddress = filterAddressInput(it)
                            }
                            prospectNeighborhoodAddressSupportingText = null
                            showProspectNeighborhoodAddressError = false
                        },
                        supportingText = {
                            prospectNeighborhoodAddressSupportingText?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                            }
                        },
                        isError = showProspectNeighborhoodAddressError,
                        keyboardType = KeyboardType.Text,
                        keyboardCapitalization = KeyboardCapitalization.Words,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = prospectZipCode,
                        label = "Zip Code",
                        leadingIcon = Icons.Default.LocalPostOffice,
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_ZIP_CODE) {
                                if (it.isDigitsOnly()) prospectZipCode = it
                            }
                            prospectZipCodeSupportingText = null
                            showProspectZipCodeError = false
                        },
                        supportingText = {
                            prospectZipCodeSupportingText?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                            }
                        },
                        isError = showProspectZipCodeError,
                        keyboardType = KeyboardType.Number,
                        keyboardCapitalization = KeyboardCapitalization.None,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = prospectPhoneNumber,
                        label = "Phone Number",
                        leadingIcon = Icons.Default.Phone,
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_PHONE_NUMBER) {
                                if (it.isDigitsOnly()) prospectPhoneNumber = it
                            }
                            prospectPhoneNumberSupportingText = null
                            showProspectPhoneNumberError = false
                        },
                        supportingText = {
                            prospectPhoneNumberSupportingText?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                            }
                        },
                        isError = showProspectPhoneNumberError,
                        keyboardType = KeyboardType.Phone,
                        keyboardCapitalization = KeyboardCapitalization.None,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = prospectRFC,
                        label = "RFC",
                        onTextChange = {
                            if (it.length <= MAX_CHARACTERS_BY_RFC) {
                                prospectRFC = filterLettersAndNumbers(it)
                            }
                            prospectRFCSupportingText = null
                            showProspectRFCError = false
                        },
                        supportingText = {
                            prospectRFCSupportingText?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = assistantFamily,
                                )
                            }
                        },
                        isError = showProspectRFCError,
                        keyboardType = KeyboardType.Text,
                        keyboardCapitalization = KeyboardCapitalization.Characters,
                    )

                    /*Row(
                        modifier = Modifier.padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "Documentos")
                        TextButton(onClick = {
                            showDocumentSection = !showDocumentSection
                        }) {
                            Text(text = if (showDocumentSection) "-" else "+")
                        }
                    }

                    if (showDocumentSection) {
                        DocumentSection(
                            documentName = documentName,
                            onDocumentNameChange = {
                                documentName = it
                                documentNameSupportingText = null
                                showDocumentNameError = false
                            },
                            onAddDocument = {
                                pickDocument.launch("*'/'*")
                            },
                        )
                    }

                    if (documentUri != null) {
                        Text(
                            "Selected Document: ${documentUri?.path}",
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                    }*/

                    Button(
                        onClick = {
                            var allValidationsPassed = true

                            if (prospectName.isEmpty()) {
                                prospectNameSupportingText = "Prospect name is required"
                                showProspectNameError = true
                                allValidationsPassed = false
                            }
                            if (prospectSurname.isEmpty()) {
                                prospectSurnameSupportingText = "Prospect surname is required"
                                showProspectSurnameError = true
                                allValidationsPassed = false
                            }
                            if (prospectStreetAddress.isEmpty()) {
                                prospectStreetAddressSupportingText =
                                    "Prospect street address is required"
                                showProspectStreetAddressError = true
                                allValidationsPassed = false
                            }
                            if (prospectNumberAddress.isEmpty()) {
                                prospectNumberAddressSupportingText =
                                    "Prospect number address is required"
                                showProspectNumberAddressError = true
                                allValidationsPassed = false
                            }
                            if (prospectNeighborhoodAddress.isEmpty()) {
                                prospectNeighborhoodAddressSupportingText =
                                    "Prospect neighborhood address is required"
                                showProspectNeighborhoodAddressError = true
                                allValidationsPassed = false
                            }
                            if (prospectZipCode.isEmpty()) {
                                prospectZipCodeSupportingText = "Prospect zip code is required"
                                showProspectZipCodeError = true
                                allValidationsPassed = false
                            }
                            if (prospectPhoneNumber.isEmpty()) {
                                prospectPhoneNumberSupportingText =
                                    "Prospect phone number is required"
                                showProspectPhoneNumberError = true
                                allValidationsPassed = false
                            }
                            if (prospectRFC.isEmpty()) {
                                prospectRFCSupportingText = "Prospect RFC is required"
                                showProspectRFCError = true
                                allValidationsPassed = false
                            }

                            if (allValidationsPassed) {
                                /*val mimeType = documentUri?.let { getMimeType(context, it) }
                                val metadata = documentUri?.let { getMetadata(context, it) }

                                if (documentUri != null && mimeType != null && metadata != null) {
                                    val fileBytes =
                                        context.contentResolver.openInputStream(documentUri!!)
                                            ?.readBytes()
                                    if (fileBytes != null) {
                                        documentViewModel.saveDocument(
                                            prospectId = prospectId,
                                            name = documentName,
                                            fileByBytes = fileBytes,
                                            fileType = mimeType,
                                            metadata = metadata,
                                        )
                                    }
                                }*/

                                val newProspect = Prospect(
                                    id = prospectId,
                                    idPromotor = promoterId!!,
                                    nombre = prospectName,
                                    primerApellido = prospectSurname,
                                    segundoApellido = prospectSecondSurname,
                                    calle = prospectStreetAddress,
                                    numero = prospectNumberAddress,
                                    colonia = prospectNeighborhoodAddress,
                                    codigoPostal = prospectZipCode,
                                    telefono = prospectPhoneNumber,
                                    rfc = prospectRFC,
                                    estatus = ProspectStatus.ENVIADO,
                                )
                                prospectViewModel.createProspect(newProspect)

                                Toast.makeText(context, "Prospecto Creado", Toast.LENGTH_LONG)
                                    .show()
                                // Toast.makeText(context, "Documento Guardado", Toast.LENGTH_LONG).show()
                                navController.navigate(AppScreens.ProspectsScreen.name)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = "Send",
                            fontWeight = FontWeight.Bold,
                            fontFamily = assistantFamily,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DocumentSection(
    documentName: String = "",
    onDocumentNameChange: (String) -> Unit = {},
    onAddDocument: () -> Unit = {},
    onUploadDocument: () -> Unit = {},
    onRemoveDocument: (String) -> Unit = {},
) {
    var documentList by remember { mutableStateOf(mutableListOf<String>()) }
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Document Name Input
        OutlinedTextField(
            value = documentName,
            onValueChange = { onDocumentNameChange(it) },
            label = { Text("Document Name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.InsertDriveFile,
                    contentDescription = null,
                )
            },
            shape = RoundedCornerShape(8.dp),
        )

        Button(
            onClick = {
                onAddDocument()
            },
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(imageVector = Icons.Rounded.AddCircle, contentDescription = "Add document")
        }
    }
}
