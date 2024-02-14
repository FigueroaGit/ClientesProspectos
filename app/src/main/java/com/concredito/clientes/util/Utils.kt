package com.concredito.clientes.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.graphics.Color
import com.concredito.clientes.util.Constants.LIST_OF_CHARACTERS_WITH_ACCENT
import com.concredito.clientes.util.Constants.LIST_OF_SPECIAL_CHARACTERS
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