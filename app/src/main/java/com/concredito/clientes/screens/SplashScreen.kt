package com.concredito.clientes.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.ui.theme.assistantFamily
import kotlinx.coroutines.delay

@Preview
@Composable
fun SplashScreen(
    navController: NavHostController,
) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                },
            ),
        )
        delay(2000L)
        navController.navigate(AppScreens.LoginScreen.name)
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(
                id = if (!isSystemInDarkTheme()) {
                    R.drawable.finacredito_logo_light_theme
                } else {
                    R.drawable.finacredito_logo_dark_theme
                },
            ),
            contentDescription = "FinaCredito logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(272.dp)
                .scale(scale.value),
        )
    }

    Box {
        Text(
            text = "Por FigueroaGit",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            fontWeight = FontWeight.Normal,
            fontFamily = assistantFamily,
        )
    }
}
