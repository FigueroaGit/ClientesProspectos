package com.concredito.clientes.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.compose.ui.graphics.Color
import com.concredito.clientes.util.Constants.LIST_OF_CHARACTERS_WITH_ACCENT
import com.concredito.clientes.util.Constants.LIST_OF_SPECIAL_CHARACTERS
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.DecimalFormat
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