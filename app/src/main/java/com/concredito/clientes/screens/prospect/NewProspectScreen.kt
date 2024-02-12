package com.concredito.clientes.screens.prospect

import ExitDialog
import FileItemUpload
import FormInputText
import ProspectsAppBar
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalPostOffice
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
import com.concredito.clientes.ui.theme.Dimens.lazyColumnBottomPadding
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.UUID

//TODO: Refactor New Prospect Screen

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

    var selectedFilesUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Listener for results of file selector
    val resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uris = data?.clipData?.let { clipData ->
                    (0 until clipData.itemCount).map { index ->
                        clipData.getItemAt(index).uri
                    }
                } ?: listOf(data?.data).filterNotNull()
                // Update the status with the updated list of selected URIs.
                selectedFilesUris += uris
            }
        }

    var showExitDialog by remember { mutableStateOf(false) }

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = lazyColumnBottomPadding)
                    .fillMaxSize(),
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(
                            start = dimenNormal,
                            end = dimenNormal,
                            bottom = dimenSmall,
                        ),
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

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            if (selectedFilesUris.isEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.AttachFile,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.outline
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "Documentos",
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                    FilledTonalIconButton(onClick = {
                                        // Open the file selector
                                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                                        intent.type = "*/*"
                                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                                        val chooserIntent =
                                            Intent.createChooser(intent, "Seleccionar archivo")
                                        // Check if there's any file selector available.
                                        if (intent.resolveActivity(context.packageManager) != null) {
                                            // Listen to the result of the file selector.
                                            resultLauncher.launch(chooserIntent)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "No se encontró una aplicación para seleccionar archivos",
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Add,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(bottom = dimenSmall),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.AttachFile,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.outline
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "Documentos seleccionados",
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = assistantFamily,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                    LazyRow(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        items(
                                            items = selectedFilesUris.distinctBy {
                                                it.toString()
                                            }
                                        ) { selectedFileUri ->
                                            val file = File(selectedFileUri.path).name
                                            val filename = file.substringAfterLast(":")
                                            val fileType =
                                                context.contentResolver.getType(selectedFileUri)
                                                    ?: "application/octet-stream"
                                            val extension =
                                                fileType.substringAfterLast("/").uppercase()
                                            FileItemUpload(
                                                name = filename,
                                                icon =
                                                when (extension) {
                                                    "PDF" -> {
                                                        painterResource(id = R.drawable.ic_file_pdf_box)
                                                    }

                                                    else -> {
                                                        painterResource(id = R.drawable.ic_image)
                                                    }
                                                },
                                                contentType = extension
                                            )
                                        }
                                        item {
                                            FilledTonalIconButton(onClick = {
                                                // Open the file selector
                                                val intent = Intent(Intent.ACTION_GET_CONTENT)
                                                intent.type = "*/*"
                                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                                intent.addCategory(Intent.CATEGORY_OPENABLE)
                                                val chooserIntent =
                                                    Intent.createChooser(
                                                        intent,
                                                        "Seleccionar archivo"
                                                    )
                                                // Check if there's any file selector available.
                                                if (intent.resolveActivity(context.packageManager) != null) {
                                                    // Escucha el resultado del selector de archivos
                                                    resultLauncher.launch(chooserIntent)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "No se encontró una aplicación para seleccionar archivos",
                                                        Toast.LENGTH_SHORT,
                                                    ).show()
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Add,
                                                    contentDescription = ""
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = dimenNormal, topEnd = dimenNormal),
            ) {
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
                        if (selectedFilesUris == null) {
                            allValidationsPassed = false
                        }

                        if (allValidationsPassed) {
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

                            selectedFilesUris?.forEach {
                                val inputStream =
                                    context.contentResolver.openInputStream(it)
                                val filePathName = File(it.path).name
                                val filename = filePathName.substringAfterLast(":")
                                val file = File(
                                    context.cacheDir,
                                    filename
                                )
                                inputStream?.use { input ->
                                    file.outputStream().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                                val contentType = context.contentResolver.getType(it)
                                    ?: "application/octet-stream"
                                val requestBody =
                                    file.asRequestBody(contentType.toMediaType())
                                val multipartBody = MultipartBody.Part.createFormData(
                                    "archivo",
                                    file.name,
                                    requestBody,
                                )
                                documentViewModel.uploadDocument(
                                    multipartBody,
                                    file.name,
                                    prospectId,
                                )
                            }

                            Toast.makeText(context, "Documento Guardado", Toast.LENGTH_LONG).show()
                            navController.navigate(AppScreens.ProspectsScreen.name + "/$promoterId")
                        }
                    },
                    modifier = Modifier.padding(dimenNormal),
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
