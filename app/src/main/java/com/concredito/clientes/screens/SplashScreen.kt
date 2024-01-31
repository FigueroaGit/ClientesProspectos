package com.concredito.clientes.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.navigation.AppScreens
import kotlinx.coroutines.delay

@Preview
@Composable
fun SplashScreen(
    navController: NavHostController
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

    Image(
        painter = painterResource(id = R.drawable.finacredito_logo),
        contentDescription = "FinaCredito logo",
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(256.dp).scale(scale.value),
    )
}
