import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.concredito.clientes.R
import com.concredito.clientes.ui.theme.Dimens.dimenNormal
import com.concredito.clientes.ui.theme.Dimens.dimenSmall
import com.concredito.clientes.ui.theme.Dimens.letterTileSize
import com.concredito.clientes.ui.theme.Dimens.letterTileSize3x
import com.concredito.clientes.ui.theme.Fonts.fontSizeLarge
import com.concredito.clientes.ui.theme.Fonts.fontSizeMedium
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.Constants.EMPTY_STRING
import com.concredito.clientes.util.Constants.LETTER_TILE_FONT_SIZE
import com.concredito.clientes.util.Constants.LETTER_TILE_FONT_SIZE_3X
import com.concredito.clientes.util.Constants.ONE_LINE
import com.concredito.clientes.util.Constants.SPLIT_DELIMITER
import com.concredito.clientes.util.getRandomColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProspectsAppBar(
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
                    horizontalArrangement = Arrangement.spacedBy(dimenSmall),
                ) {
                    val titleWithGreetings = title.split(SPLIT_DELIMITER)
                    val usernameLetter = titleWithGreetings[1]

                    LetterTile(
                        text = usernameLetter.take(1).uppercase(),
                        size = letterTileSize,
                        fontSize = LETTER_TILE_FONT_SIZE,
                    )
                    Text(
                        text = title,
                        fontWeight = FontWeight.Medium,
                        fontFamily = assistantFamily,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = fontSizeLarge,
                    )
                }
            } else {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontFamily = assistantFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = fontSizeLarge,
                )
            }
        },
        navigationIcon = {
            if (!isHome) {
                IconButton(onClick = { onBackPressed.invoke() }) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(id = R.string.back_content_description),
                        )
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
                        contentDescription = stringResource(id = R.string.logout_content_description),
                    )
                }
            }
        },
    )
}

@Composable
fun FormInputText(
    modifier: Modifier = Modifier,
    text: String = EMPTY_STRING,
    label: String = EMPTY_STRING,
    leadingIcon: ImageVector = Icons.Rounded.Person,
    maxLine: Int = ONE_LINE,
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
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = stringResource(id = R.string.leading_icon_content_description),
            )
        },
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
        shape = RoundedCornerShape(dimenSmall),
        maxLines = maxLine,
    )
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String,
    onClickArrow: () -> Unit,
    isSectionVisible: Boolean,
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimenNormal),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.primary,
                fontSize = fontSizeMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = assistantFamily,
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(1F),
            )
            IconButton(onClick = { onClickArrow() }) {
                Icon(
                    imageVector =
                    if (isSectionVisible) {
                        Icons.Rounded.KeyboardArrowDown
                    } else {
                        Icons.Rounded.KeyboardArrowUp
                    },
                    contentDescription = stringResource(id = R.string.section_visible_content_description),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
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
            Text(
                text = stringResource(id = R.string.exit_confirmation),
                fontFamily = assistantFamily,
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.exit_confirmation_text),
                fontFamily = assistantFamily,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
            ) {
                Text(
                    text = stringResource(id = R.string.exit_confirmation_button_yes),
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
            ) {
                Text(
                    text = stringResource(id = R.string.exit_confirmation_button_no),
                    fontWeight = FontWeight.Bold,
                    fontFamily = assistantFamily,
                )
            }
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProspectsLargeTopAppBar(
    title: String,
    letterTileSize: Dp = letterTileSize3x, // Default size for the LetterTile
    fontSize: Int = LETTER_TILE_FONT_SIZE_3X,
    additionalText: String = EMPTY_STRING, // Additional text to be displayed
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
                val titleWithGreetings = title.split(SPLIT_DELIMITER)
                val usernameLetter = titleWithGreetings[1]
                LetterTile(
                    text = usernameLetter.take(1).uppercase(),
                    size = letterTileSize,
                    fontSize = fontSize,
                )
                Column(modifier = Modifier.padding(horizontal = dimenNormal)) {
                    Text(
                        text = title,
                        fontFamily = assistantFamily,
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onShowProspectsClicked.invoke() },
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_show_all_prospects),
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
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = stringResource(id = R.string.menu_icon_content_description),
                    )
                }

                if (additionalText.isNotEmpty()) {
                    Text(
                        text = additionalText,
                        fontWeight = FontWeight.Medium,
                        fontFamily = assistantFamily,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = fontSizeLarge,
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onLogoutClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = stringResource(id = R.string.logout_content_description),
                )
            }
        },
    )
}

@Composable
fun LetterTile(text: String, size: Dp, fontSize: Int, modifier: Modifier = Modifier) {
    val randomColor = getRandomColor()

    Box(
        modifier = modifier
            .size(size)
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
                .padding(dimenSmall)
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = fontSize.sp,
        )
    }
}
