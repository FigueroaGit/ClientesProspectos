package com.concredito.clientes.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.ui.theme.Dimens.densityPixels16
import com.concredito.clientes.ui.theme.Dimens.densityPixels256
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.Constants.DELAY_TIME_TWO_SECONDS
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        animateLogo(scale)
        delayAndNavigate(navController)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(densityPixels16),
        contentAlignment = Alignment.Center,
    ) {
        val logoResourceId = if (!isSystemInDarkTheme()) {
            R.drawable.finacredito_logo_light_theme
        } else {
            R.drawable.finacredito_logo_dark_theme
        }

        Image(
            painter = painterResource(id = logoResourceId),
            contentDescription = stringResource(id = R.string.logo_image_content_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(densityPixels256)
                .scale(scale.value),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = densityPixels16),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            text = stringResource(id = R.string.app_creator_name),
            fontWeight = FontWeight.Normal,
            fontFamily = assistantFamily,
        )
    }
}

private suspend fun animateLogo(scale: Animatable<Float, AnimationVector1D>) {
    scale.animateTo(
        targetValue = 0.9f,
        animationSpec = tween(
            durationMillis = 800,
            easing = { easing ->
                OvershootInterpolator(8f)
                    .getInterpolation(easing)
            },
        ),
    )
}

private suspend fun delayAndNavigate(navController: NavHostController) {
    delay(DELAY_TIME_TWO_SECONDS)
    navController.navigate(AppScreens.LoginScreen.name)
}
