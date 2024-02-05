package com.concredito.clientes.screens.prospect

import ExitDialog
import FormInputText
import ProspectsAppBar
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalPostOffice
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Phone
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getString
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.model.ProspectStatus
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.ui.theme.Dimens.dimenExtraSmall
import com.concredito.clientes.ui.theme.Dimens.dimenNormal
import com.concredito.clientes.ui.theme.Dimens.dimenSmall
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.Constants.EMPTY_STRING
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
fun NewProspectScreen(
    navController: NavHostController,
    prospectsViewModel: ProspectsViewModel = hiltViewModel(),
    documentViewModel: DocumentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val prospectId = rememberSaveable { UUID.randomUUID().toString() }
    val promoterId = prospectsViewModel.getPromoterId()
    var prospectName by rememberSaveable { mutableStateOf("") }
    var prospectSurname by rememberSaveable { mutableStateOf("") }
    var prospectSecondSurname by rememberSaveable { mutableStateOf("") }
    var prospectStreetAddress by rememberSaveable { mutableStateOf("") }
    var prospectNumberAddress by rememberSaveable { mutableStateOf("") }
    var prospectNeighborhoodAddress by rememberSaveable { mutableStateOf("") }
    var prospectZipCode by rememberSaveable { mutableStateOf("") }
    var prospectPhoneNumber by rememberSaveable { mutableStateOf("") }
    var prospectRFC by rememberSaveable { mutableStateOf("") }

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
                navController.navigate(AppScreens.MainScreen.name)
            },
            onDismiss = {
                showExitDialog = false
            },
        )
    }

    Scaffold(topBar = {
        ProspectsAppBar(
            title = stringResource(id = R.string.new_prospect_screen_app_bar_text),
            isHome = false,
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
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
                    navController.navigate(AppScreens.MainScreen.name)
                } else {
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
                        .padding(start = dimenNormal, end = dimenNormal, bottom = dimenNormal),
                ) {
                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimenExtraSmall),
                        text = prospectName,
                        label = stringResource(id = R.string.label_name_field),
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_NAME) {
                                prospectName = filterNameInput(text)
                            }
                            prospectNameSupportingText = null
                            showProspectNameError = false
                        },
                        supportingText = {
                            prospectNameSupportingText?.let { errorMessage ->
                                Text(
                                    text = (errorMessage),
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
                            .padding(vertical = dimenExtraSmall),
                        text = prospectSurname,
                        label = stringResource(id = R.string.label_surname_field),
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_NAME) {
                                prospectSurname = filterNameInput(text)
                            }
                            prospectSurnameSupportingText = null
                            showProspectSurnameError = false
                        },
                        supportingText = {
                            prospectSurnameSupportingText?.let { errorMessage ->
                                Text(
                                    text = errorMessage,
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
                            .padding(vertical = dimenExtraSmall),
                        text = prospectSecondSurname,
                        label = stringResource(id = R.string.label_second_surname_field),
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_NAME) {
                                prospectSecondSurname = filterNameInput(text)
                            }
                        },
                        keyboardType = KeyboardType.Text,
                        keyboardCapitalization = KeyboardCapitalization.Words,
                    )

                    FormInputText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimenExtraSmall),
                        text = prospectStreetAddress,
                        label = stringResource(id = R.string.label_street_field),
                        leadingIcon = Icons.Rounded.Home,
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_ADDRESS) {
                                prospectStreetAddress = filterAddressInput(text)
                            }
                            prospectStreetAddressSupportingText = null
                            showProspectStreetAddressError = false
                        },
                        supportingText = {
                            prospectStreetAddressSupportingText?.let { errorMessage ->
                                Text(
                                    text = errorMessage,
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
                            .padding(vertical = dimenExtraSmall),
                        text = prospectNumberAddress,
                        label = stringResource(id = R.string.label_number_field),
                        leadingIcon = Icons.Rounded.Numbers,
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_NUMBER_ADDRESS) {
                                prospectNumberAddress = filterLettersAndNumbers(text)
                            }
                            prospectNumberAddressSupportingText = null
                            showProspectNumberAddressError = false
                        },
                        supportingText = {
                            prospectNumberAddressSupportingText?.let { errorMessage ->
                                Text(
                                    text = errorMessage,
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
                            .padding(vertical = dimenExtraSmall),
                        text = prospectNeighborhoodAddress,
                        label = stringResource(id = R.string.label_neighborhood_field),
                        leadingIcon = Icons.Rounded.Home,
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_ADDRESS) {
                                prospectNeighborhoodAddress = filterAddressInput(text)
                            }
                            prospectNeighborhoodAddressSupportingText = null
                            showProspectNeighborhoodAddressError = false
                        },
                        supportingText = {
                            prospectNeighborhoodAddressSupportingText?.let { errorMessage ->
                                Text(
                                    text = errorMessage,
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
                            .padding(vertical = dimenExtraSmall),
                        text = prospectZipCode,
                        label = stringResource(id = R.string.label_zip_code_field),
                        leadingIcon = Icons.Rounded.LocalPostOffice,
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_ZIP_CODE) {
                                if (text.isDigitsOnly()) prospectZipCode = text
                            }
                            prospectZipCodeSupportingText = null
                            showProspectZipCodeError = false
                        },
                        supportingText = {
                            prospectZipCodeSupportingText?.let { errorMessage ->
                                Text(
                                    text = errorMessage,
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
                            .padding(vertical = dimenExtraSmall),
                        text = prospectPhoneNumber,
                        label = stringResource(id = R.string.label_phone_number_field),
                        leadingIcon = Icons.Rounded.Phone,
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_PHONE_NUMBER) {
                                if (text.isDigitsOnly()) prospectPhoneNumber = text
                            }
                            prospectPhoneNumberSupportingText = null
                            showProspectPhoneNumberError = false
                        },
                        supportingText = {
                            prospectPhoneNumberSupportingText?.let { errorMessage ->
                                Text(
                                    text = errorMessage,
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
                            .padding(vertical = dimenExtraSmall),
                        text = prospectRFC,
                        label = stringResource(id = R.string.label_rfc_field),
                        onTextChange = { text ->
                            if (text.length <= MAX_CHARACTERS_BY_RFC) {
                                prospectRFC = filterLettersAndNumbers(text)
                            }
                            prospectRFCSupportingText = null
                            showProspectRFCError = false
                        },
                        supportingText = {
                            prospectRFCSupportingText?.let { errorMessage ->
                                Text(
                                    text = errorMessage,
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
                                prospectNameSupportingText =
                                    getString(context, R.string.name_error_text)
                                showProspectNameError = true
                                allValidationsPassed = false
                            }
                            if (prospectSurname.isEmpty()) {
                                prospectSurnameSupportingText =
                                    getString(context, R.string.surname_error_text)
                                showProspectSurnameError = true
                                allValidationsPassed = false
                            }
                            if (prospectStreetAddress.isEmpty()) {
                                prospectStreetAddressSupportingText =
                                    getString(context, R.string.address_error_text)
                                showProspectStreetAddressError = true
                                allValidationsPassed = false
                            }
                            if (prospectNumberAddress.isEmpty()) {
                                prospectNumberAddressSupportingText =
                                    getString(context, R.string.number_error_text)
                                showProspectNumberAddressError = true
                                allValidationsPassed = false
                            }
                            if (prospectNeighborhoodAddress.isEmpty()) {
                                prospectNeighborhoodAddressSupportingText =
                                    getString(context, R.string.neighborhood_error_text)
                                showProspectNeighborhoodAddressError = true
                                allValidationsPassed = false
                            }
                            if (prospectZipCode.isEmpty()) {
                                prospectZipCodeSupportingText =
                                    getString(context, R.string.zip_code_error_text)
                                showProspectZipCodeError = true
                                allValidationsPassed = false
                            }
                            if (prospectPhoneNumber.isEmpty()) {
                                prospectPhoneNumberSupportingText =
                                    getString(context, R.string.phone_number_error_text)
                                showProspectPhoneNumberError = true
                                allValidationsPassed = false
                            }
                            if (prospectRFC.isEmpty()) {
                                prospectRFCSupportingText =
                                    getString(context, R.string.rfc_error_text)
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
                                    promoterId = promoterId!!,
                                    name = prospectName,
                                    surname = prospectSurname,
                                    secondSurname = prospectSecondSurname,
                                    streetAddress = prospectStreetAddress,
                                    numberAddress = prospectNumberAddress,
                                    neighborhood = prospectNeighborhoodAddress,
                                    zipCode = prospectZipCode,
                                    phoneNumber = prospectPhoneNumber,
                                    rfc = prospectRFC,
                                    status = ProspectStatus.ENVIADO,
                                )
                                prospectsViewModel.createProspect(newProspect)

                                Toast.makeText(
                                    context,
                                    getString(context, R.string.message_send_prospect_data),
                                    Toast.LENGTH_LONG,
                                )
                                    .show()
                                // Toast.makeText(context, "Documento Guardado", Toast.LENGTH_LONG).show()
                                navController.navigate(AppScreens.ProspectsScreen.name + "/$promoterId")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Send,
                            contentDescription = stringResource(id = R.string.send_icon_content_description),
                        )
                        Spacer(modifier = Modifier.width(dimenSmall))
                        Text(
                            text = stringResource(id = R.string.button_send_to_evaluation_area),
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
    documentName: String = EMPTY_STRING,
    onDocumentNameChange: (String) -> Unit = {},
    onAddDocument: () -> Unit = {},
    onUploadDocument: () -> Unit = {},
    onRemoveDocument: (String) -> Unit = {},
) {
    var documentList by remember { mutableStateOf(mutableListOf<String>()) }
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(vertical = dimenSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Document Name Input
        OutlinedTextField(
            value = documentName,
            onValueChange = { onDocumentNameChange(it) },
            label = { Text(text = stringResource(id = R.string.label_document_name_field)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.InsertDriveFile,
                    contentDescription = stringResource(id = R.string.document_icon_content_description),
                )
            },
            shape = RoundedCornerShape(dimenSmall),
        )

        Button(
            onClick = {
                onAddDocument()
            },
            shape = RoundedCornerShape(dimenSmall),
        ) {
            Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = stringResource(id = R.string.add_document_icon_content_description),
            )
        }
    }
}
