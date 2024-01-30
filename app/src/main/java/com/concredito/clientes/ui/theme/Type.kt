package com.concredito.clientes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.concredito.clientes.R

val assistantFamily = FontFamily(
    Font(R.font.assistant_extra_light, FontWeight.ExtraLight),
    Font(R.font.assistant_light, FontWeight.Light),
    Font(R.font.assistant_regular, FontWeight.Normal),
    Font(R.font.assistant_medium, FontWeight.Medium),
    Font(R.font.assistant_semi_bold, FontWeight.SemiBold),
    Font(R.font.assistant_bold, FontWeight.Bold),
    Font(R.font.assistant_extra_bold, FontWeight.ExtraBold),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
