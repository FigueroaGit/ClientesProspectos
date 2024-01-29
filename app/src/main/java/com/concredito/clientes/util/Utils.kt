package com.concredito.clientes.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.ui.graphics.Color
import com.concredito.clientes.util.Constants.LIST_OF_CHARACTERS_WITH_ACCENT
import com.concredito.clientes.util.Constants.LIST_OF_SPECIAL_CHARACTERS
import java.io.File
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

fun getMimeType(context: Context, uri: Uri): String? {
    val contentResolver: ContentResolver = context.contentResolver

    // Get MIME type from the ContentResolver
    var mimeType: String? = contentResolver.getType(uri)

    // If MIME type is null, try to infer it from the file extension
    if (mimeType == null) {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
    }

    return mimeType
}

fun getMetadata(context: Context, uri: Uri): String? {
    // Example: Retrieve file size from metadata
    val fileSize: Long = File(uri.path).length()

    // You can customize this part based on the metadata you want to extract
    return "File Size: $fileSize bytes"
}
