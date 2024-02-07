package com.concredito.clientes.screens.login

import LoadingScreenDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.concredito.clientes.R
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.data.Resource
import com.concredito.clientes.navigation.AppScreens
import com.concredito.clientes.screens.prospect.PromoterViewModel
import com.concredito.clientes.ui.theme.Dimens.dimenMedium
import com.concredito.clientes.ui.theme.Dimens.dimenNormal
import com.concredito.clientes.ui.theme.Dimens.dimenSmall
import com.concredito.clientes.ui.theme.Dimens.imageSizeSmall
import com.concredito.clientes.ui.theme.Fonts.fontSizeExtraLarge
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.Constants.DELAY_TIME_TWO_SECONDS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    promoterViewModel: PromoterViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val preferencesManager = PreferencesManager(context)
    val savedPromoterId = preferencesManager.getPromoterId()
    val savedUsername = preferencesManager.getUsername()

    if (!savedPromoterId.isNullOrEmpty() && !savedUsername.isNullOrEmpty()) {
        navController.navigate(AppScreens.MainScreen.name)
    } else {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimenNormal)
                .statusBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(dimenNormal))

            Image(
                painter = painterResource(
                    id = if (!isSystemInDarkTheme()) {
                        R.drawable.finacredito_logo_light_theme
                    } else {
                        R.drawable.finacredito_logo_dark_theme
                    },
                ),
                contentDescription = stringResource(id = R.string.logo_image_content_description),
                modifier = Modifier
                    .width(imageSizeSmall)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = dimenNormal),
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(vertical = dimenSmall),
                text = stringResource(id = R.string.welcome_login_text),
                fontSize = fontSizeExtraLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
                textAlign = TextAlign.Center,
            )

            OutlinedTextField(
                value = username,
                onValueChange = { user ->
                    username = user
                    usernameSupportingText = null
                    showUsernameError = false
                    showError = false
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.label_username_field),
                        fontWeight = FontWeight.Normal,
                        fontFamily = assistantFamily,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = stringResource(id = R.string.leading_icon_content_description),
                    )
                },
                supportingText = {
                    usernameSupportingText?.let { advice ->
                        Text(
                            text = advice,
                            fontWeight = FontWeight.Normal,
                            fontFamily = assistantFamily,
                        )
                    }
                },
                isError = showUsernameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimenSmall),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {},
                ),
                shape = RoundedCornerShape(dimenSmall),
            )

            OutlinedTextField(
                value = password,
                onValueChange = { passwd ->
                    password = passwd
                    passwordSupportingText = null
                    showPasswordError = false
                    showError = false
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.label_password_field),
                        fontWeight = FontWeight.Normal,
                        fontFamily = assistantFamily,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = stringResource(
                            id = R.string.leading_icon_content_description,
                        ),
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        Icon(
                            imageVector =
                            if (isPasswordVisible) {
                                Icons.Rounded.Visibility
                            } else {
                                Icons.Rounded.VisibilityOff
                            },
                            contentDescription =
                            if (isPasswordVisible) {
                                stringResource(id = R.string.password_invisible_content_description)
                            } else {
                                stringResource(id = R.string.password_visible_content_description)
                            },
                        )
                    }
                },
                supportingText = {
                    passwordSupportingText?.let { advice ->
                        Text(
                            text = advice,
                            fontWeight = FontWeight.Normal,
                            fontFamily = assistantFamily,
                        )
                    }
                },
                isError = showPasswordError,
                visualTransformation =
                if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimenNormal),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    },
                ),
                shape = RoundedCornerShape(dimenSmall),
            )

            Text(
                text = stringResource(id = R.string.forgot_password),
                fontWeight = FontWeight.Medium,
                fontFamily = assistantFamily,
                modifier = Modifier.padding(bottom = dimenMedium).clickable {},
                color = MaterialTheme.colorScheme.primary,
            )

            Button(
                onClick = {
                    if (isLoading) {
                        return@Button
                    }

                    if (username.isEmpty()) {
                        usernameSupportingText = getString(context, R.string.advice_username_text)
                        showUsernameError = true
                        return@Button
                    }
                    if (password.isEmpty()) {
                        passwordSupportingText = getString(context, R.string.advice_password_text)
                        showPasswordError = true
                        return@Button
                    }

                    showDialog = true

                    promoterViewModel.viewModelScope.launch {
                        delay(DELAY_TIME_TWO_SECONDS)
                        val result = promoterViewModel.login(username, password)

                        if (result is Resource.Success) {
                            val response = result.data
                            // Save promoter id and username from the response
                            val promoterId = response?.id
                            val loggedInUsername = response?.username

                            // Save Promoter ID and Username using PreferencesManager
                            if (!promoterId.isNullOrEmpty() && !loggedInUsername.isNullOrEmpty()) {
                                PreferencesManager(context).savePromoterInfo(
                                    promoterId,
                                    loggedInUsername,
                                )
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
                    text = stringResource(id = R.string.login_button_text),
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                )
            }

            if (showError) {
                Text(
                    text = stringResource(id = R.string.login_error_text),
                    fontWeight = FontWeight.Normal,
                    fontFamily = assistantFamily,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(dimenSmall),
                )
            }

            if (showDialog) {
                LoadingScreenDialog()
            }
        }
    }
}
