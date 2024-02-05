package com.concredito.clientes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.concredito.clientes.navigation.AppNavigation
import com.concredito.clientes.ui.theme.ClientesProspectosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientesProspectosTheme {
                FinaCreditoApp()
            }
        }
    }
}

@Composable
fun FinaCreditoApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        AppNavigation()
    }
}
