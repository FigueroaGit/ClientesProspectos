package com.concredito.clientes.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.data.Resource
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.screens.prospect.PromoterViewModel
import com.concredito.clientes.ui.theme.assistantFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    promoterViewModel: PromoterViewModel = hiltViewModel(),
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }
    var usernameSupportingText by remember { mutableStateOf<String?>(null) }
    var passwordSupportingText by remember { mutableStateOf<String?>(null) }

    var showUsernameError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val preferencesManager = PreferencesManager(context)

    // Verifica si el ID del promotor y el nombre de usuario ya están almacenados
    val savedPromoterId = preferencesManager.getPromoterId()
    val savedUsername = preferencesManager.getUsername()

    if (savedPromoterId != null && savedUsername != null) {
        // Si las credenciales están guardadas, navega directamente a la pantalla principal
        navController.navigate(AppScreens.MainScreen.name)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(
                    id = if (!isSystemInDarkTheme()) {
                        R.drawable.finacredito_logo_light_theme
                    } else {
                        R.drawable.finacredito_logo_dark_theme
                    },
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(256.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp),
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                text = "Bienvenido de nuevo",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
                textAlign = TextAlign.Center,
            )

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameSupportingText = null
                    showUsernameError = false
                    showError = false
                },
                label = {
                    Text(
                        text = "Usuario",
                        fontWeight = FontWeight.Normal,
                        fontFamily = assistantFamily,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                    )
                },
                supportingText = {
                    usernameSupportingText?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Normal,
                            fontFamily = assistantFamily,
                        )
                    }
                },
                isError = showUsernameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {},
                ),
                shape = RoundedCornerShape(8.dp),
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordSupportingText = null
                    showPasswordError = false
                    showError = false
                },
                label = {
                    Text(
                        "contraseña",
                        fontWeight = FontWeight.Normal,
                        fontFamily = assistantFamily,
                    )
                },
                leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        )
                    }
                },
                supportingText = {
                    passwordSupportingText?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Normal,
                            fontFamily = assistantFamily,
                        )
                    }
                },
                isError = showPasswordError,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    },
                ),
                shape = RoundedCornerShape(8.dp),
            )

            Text(
                text = "Olvide la contraseña",
                fontWeight = FontWeight.Medium,
                fontFamily = assistantFamily,
                modifier = Modifier.padding(bottom = 24.dp).clickable {},
                color = MaterialTheme.colorScheme.primary,
            )

            Button(
                onClick = {
                    if (isLoading) {
                        return@Button
                    }

                    if (username.isEmpty()) {
                        usernameSupportingText = "El nombre de usuario es requerido"
                        showUsernameError = true
                        return@Button
                    }
                    if (password.isEmpty()) {
                        passwordSupportingText = "la contraseña es requerida"
                        showPasswordError = true
                        return@Button
                    }

                    showDialog = true

                    promoterViewModel.viewModelScope.launch {
                        delay(2000)
                        val result = promoterViewModel.login(username, password)

                        if (result is Resource.Success) {
                            val response = result.data
                            // Extrae el ID del promotor y el nombre de usuario del response
                            val promoterId = response?.id
                            val loggedInUsername = response?.usuario

                            // Guarda el ID del promotor y el nombre de usuario utilizando PreferencesManager
                            if (promoterId != null) {
                                if (loggedInUsername != null) {
                                    PreferencesManager(context).savePromoterInfo(
                                        promoterId,
                                        loggedInUsername,
                                    )
                                }
                            }

                            navController.navigate(AppScreens.MainScreen.name)
                        } else {
                            showError = true
                        }
                        showDialog = false
                    }

                    keyboardController?.hide()
                },
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Iniciar sesión",
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                )
            }

            if (showError) {
                Text(
                    text = "El nombre de usuario y/o la contraseña son incorrectos",
                    fontWeight = FontWeight.Normal,
                    fontFamily = assistantFamily,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp),
                )
            }

            if (showDialog) {
                Dialog(
                    onDismissRequest = {},
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(50.dp),
                        )
                    }
                }
            }
        }
    }
}
