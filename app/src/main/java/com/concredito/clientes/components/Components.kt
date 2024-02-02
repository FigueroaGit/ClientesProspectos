import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.getRandomColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProspectiveCustomerAppBar(
    title: String,
    isHome: Boolean = true,
    icon: ImageVector? = null,
    navController: NavController,
    onBackPressed: () -> Unit = {},
    onLogoutPressed: () -> Unit = {},
) {
    TopAppBar(
        title = {
            if (isHome) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Obtener la primera letra del título (nombre de usuario)
                    val titleWithGreetings = title.split(", ")
                    val usernameLetter = titleWithGreetings[1]

                    // Agregar LetterTile con la primera letra como imagen
                    LetterTile(text = usernameLetter.take(1).uppercase())
                    Text(
                        text = title,
                        fontWeight = FontWeight.Medium,
                        fontFamily = assistantFamily,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                    )
                }
            } else {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontFamily = assistantFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                )
            }
        },
        navigationIcon = {
            if (!isHome) {
                IconButton(onClick = { onBackPressed.invoke() }) {
                    if (icon != null) {
                        Icon(imageVector = icon, contentDescription = "Arrow Back")
                    }
                }
            } else {
                Box {}
            }
        },
        actions = {
            if (isHome) {
                IconButton(onClick = { onLogoutPressed.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Logout,
                        contentDescription = "Logout",
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
@Preview
fun FormInputText(
    modifier: Modifier = Modifier,
    text: String = "",
    label: String = "",
    leadingIcon: ImageVector = Icons.Default.Person,
    maxLine: Int = 1,
    onTextChange: (String) -> Unit = {},
    supportingText: @Composable () -> Unit = { },
    isError: Boolean = false,
    keyboardType: KeyboardType,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    onImeAction: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier,
        label = {
            Text(
                text = label,
                fontWeight = FontWeight.Normal,
                fontFamily = assistantFamily,
            )
        },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        supportingText = supportingText,
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = keyboardType,
            capitalization = keyboardCapitalization,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
                keyboardController?.hide()
            },
        ),
        shape = RoundedCornerShape(8.dp),
        maxLines = maxLine,
    )
}

@Composable
@Preview
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String = "Header",
    onClickArrow: () -> Unit = {},
    isSectionVisible: Boolean = true,
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(1F),
            )
            IconButton(onClick = { onClickArrow() }) {
                Icon(
                    imageVector = if (isSectionVisible) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun LetterTile(text: String) {
    val randomColor = getRandomColor()

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = randomColor,
                shape = CircleShape,
            )
            .clip(CircleShape),
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
    }
}

@Composable
@Preview
fun ExitDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text("Confirmación de salida")
        },
        text = {
            Text("¿Estás seguro de que quieres salir? Los datos no se guardarán.")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
            ) {
                Text("Si")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
            ) {
                Text("No")
            }
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProspectsLargeTopAppBar(
    title: String,
    letterTileSize: Int = 72, // Default size for the LetterTile
    fontSize: Int = 32,
    additionalText: String = "", // Additional text to be displayed
    navController: NavController,
    onLogoutClicked: () -> Unit,
    onMenuClicked: () -> Unit, // Callback for hamburger icon click
    onShowProspectsClicked: () -> Unit,
) {
    LargeTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // LetterTile on the left side with custom size
                val titleWithGreetings = title.split(", ")
                val usernameLetter = titleWithGreetings[1]
                LetterTile(
                    text = usernameLetter.take(1).uppercase(),
                    size = letterTileSize,
                    fontSize = fontSize,
                )
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = title,
                        fontFamily = assistantFamily,
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onShowProspectsClicked.invoke() },
                    ) {
                        Text(
                            text = "Mostrar todos los prospectos",
                            fontWeight = FontWeight.Bold,
                            fontFamily = assistantFamily,
                        )
                    }
                }
            }
        },
        navigationIcon = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onMenuClicked) {
                    Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Search")
                }

                if (additionalText.isNotEmpty()) {
                    Text(
                        text = additionalText,
                        fontWeight = FontWeight.Medium,
                        fontFamily = assistantFamily,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onLogoutClicked) {
                Icon(imageVector = Icons.Rounded.Logout, contentDescription = "Logout")
            }
        },
    )
}

@Composable
fun LetterTile(text: String, size: Int, fontSize: Int, modifier: Modifier = Modifier) {
    val randomColor = getRandomColor()

    Box(
        modifier = modifier
            .size(size.dp)
            .background(
                color = randomColor,
                shape = CircleShape,
            )
            .clip(CircleShape),
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = fontSize.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomLargeTopAppBarPreview() {
    ProspectsLargeTopAppBar(
        title = "Welcome, username!!",
        letterTileSize = 72, // Custom size for LetterTile
        fontSize = 32,
        additionalText = "Main Menu",
        navController = NavController(LocalContext.current),
        onLogoutClicked = {},
        onMenuClicked = {},
        onShowProspectsClicked = {},
    )
}
