package com.concredito.clientes.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.graphics.Color
import com.concredito.clientes.R
import com.concredito.clientes.model.DocumentType
import com.concredito.clientes.util.Constants.LIST_OF_CHARACTERS_WITH_ACCENT
import com.concredito.clientes.util.Constants.LIST_OF_SPECIAL_CHARACTERS
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.math.roundToLong
import kotlin.random.Random

fun getRandomColor(): Color {
    val random = Random.Default
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 1f,
    )
}

fun filterNameInput(input: String): String {
    return input.filter {
        it.isLetter() || it.isWhitespace() || it in LIST_OF_CHARACTERS_WITH_ACCENT
    }
}

fun filterAddressInput(input: String): String {
    return input.filter {
        it.isLetter() || it.isDigit() || it.isWhitespace() || it in LIST_OF_SPECIAL_CHARACTERS
    }
}

fun filterLettersAndNumbers(input: String): String {
    return input.filter {
        it.isLetterOrDigit()
    }
}

fun formatSize(sizeInBytes: Long): String {
    val kb = 1024
    val mb = kb * kb

    return when {
        sizeInBytes < kb -> "$sizeInBytes B"
        sizeInBytes < mb -> "${(sizeInBytes.toDouble() / kb).roundToLong()} KB"
        else -> "${(sizeInBytes.toDouble() / mb).roundToLong()} MB"
    }
}

fun getIconResource(extension: String): Int {
    return when (extension.uppercase()) {
        DocumentType.PDF.name -> R.drawable.ic_file_type_pdf
        DocumentType.DOC.name -> R.drawable.ic_file_type_doc
        DocumentType.TXT.name -> R.drawable.ic_file_type_txt
        DocumentType.JPEG.name -> R.drawable.ic_file_type_jpg
        DocumentType.PNG.name -> R.drawable.ic_file_type_png
        DocumentType.GIF.name -> R.drawable.ic_file_type_gif
        DocumentType.ZIP.name -> R.drawable.ic_file_type_zip
        DocumentType.RAR.name -> R.drawable.ic_file_type_rar
        else -> R.drawable.ic_file_type
    }
}

fun openFileSelector(context: Context, resultLauncher: ActivityResultLauncher<Intent>) {
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
}

fun getFileDetails(context: Context, selectedFileUri: Uri): Pair<String, String> {
    val file = File(selectedFileUri.path)
    val filename = file.name.substringAfterLast(":")
    val fileType = context.contentResolver.getType(selectedFileUri) ?: "application/octet-stream"
    return Pair(filename, fileType)
}

fun getExtension(extension: String): String {
    return extension.substringAfterLast("/")
}

fun processFile(context: Context, selectedFileUri: Uri): Pair<MultipartBody.Part, String> {
    val inputStream = context.contentResolver.openInputStream(selectedFileUri)
    val (filename, filetype) = getFileDetails(context, selectedFileUri)
    val file = File(context.cacheDir, filename)

    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    val requestBody = file.asRequestBody(filetype.toMediaType())
    val multipartBody = MultipartBody.Part.createFormData("archivo", file.name, requestBody)

    return Pair(multipartBody, file.name)
}