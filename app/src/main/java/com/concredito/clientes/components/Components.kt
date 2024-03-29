import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.concredito.clientes.R
import com.concredito.clientes.ui.theme.Dimens
import com.concredito.clientes.ui.theme.Dimens.densityPixels12
import com.concredito.clientes.ui.theme.Dimens.densityPixels154
import com.concredito.clientes.ui.theme.Dimens.densityPixels4
import com.concredito.clientes.ui.theme.Dimens.densityPixels16
import com.concredito.clientes.ui.theme.Dimens.densityPixels2
import com.concredito.clientes.ui.theme.Dimens.densityPixels256
import com.concredito.clientes.ui.theme.Dimens.densityPixels8
import com.concredito.clientes.ui.theme.Dimens.densityPixels40
import com.concredito.clientes.ui.theme.Dimens.densityPixels48
import com.concredito.clientes.ui.theme.Dimens.densityPixels72
import com.concredito.clientes.ui.theme.Dimens.densityPixels80
import com.concredito.clientes.ui.theme.Fonts
import com.concredito.clientes.ui.theme.Fonts.fontSizeExtraLarge
import com.concredito.clientes.ui.theme.Fonts.fontSizeExtraSmall
import com.concredito.clientes.ui.theme.Fonts.fontSizeLarge
import com.concredito.clientes.ui.theme.Fonts.fontSizeMedium
import com.concredito.clientes.ui.theme.Fonts.fontSizeNormal
import com.concredito.clientes.ui.theme.Fonts.fontSizeSmall
import com.concredito.clientes.ui.theme.assistantFamily
import com.concredito.clientes.util.Constants
import com.concredito.clientes.util.Constants.EMPTY_STRING
import com.concredito.clientes.util.Constants.ONE_LINE
import com.concredito.clientes.util.Constants.SPLIT_DELIMITER
import com.concredito.clientes.util.Constants.TWO_LINES

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProspectsAppBar(
    title: String,
    isHome: Boolean = true,
    icon: ImageVector? = null,
    navController: NavController,
    onBackPressed: () -> Unit = {},
) {
    TopAppBar(
        title = {
            if (isHome) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(densityPixels8),
                ) {
                    val titleWithGreetings = title.split(SPLIT_DELIMITER)
                    val usernameLetter = titleWithGreetings[1]

                    LetterTile(
                        text = usernameLetter.take(1).uppercase(),
                        size = densityPixels40,
                        fontSize = fontSizeMedium,
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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProspectsLargeTopAppBar(
    title: String,
    letterTileSize: Dp = densityPixels72,
    fontSize: TextUnit = fontSizeExtraLarge,
    additionalText: String = EMPTY_STRING,
    jobRole: String,
    navController: NavController,
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
                Column(modifier = Modifier.padding(horizontal = densityPixels16)) {
                    Text(
                        text = title,
                        fontFamily = assistantFamily,
                    )
                    Text(
                        text = jobRole,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = assistantFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSizeNormal
                    )
                }
            }
        },
        navigationIcon = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(densityPixels40),
                    painter = painterResource(id = R.drawable.ic_finacredito),
                    contentDescription = stringResource(id = R.string.icon_content_description),
                )

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
    )
}

@Composable
fun LetterTile(text: String, size: Dp, fontSize: TextUnit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = MaterialTheme.colorScheme.inversePrimary,
                shape = CircleShape,
            )
            .clip(CircleShape),
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(densityPixels8)
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = fontSize,
        )
    }
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
        shape = RoundedCornerShape(densityPixels8),
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
                .padding(horizontal = densityPixels16),
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

@Composable
fun LoadingScreenDialog() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.densityPixels128)
                .clip(RoundedCornerShape(densityPixels8))
                .background(MaterialTheme.colorScheme.background)
                .padding(densityPixels16),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.densityPixels64),
            )
        }
    }
}

@Composable
fun ObservationsDialog(
    onDismiss: () -> Unit,
    observations: String,
    onObservationsChange: (String) -> Unit,
    onClickSend: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(densityPixels16),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = densityPixels16),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = {
                            onDismiss.invoke()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(id = R.string.close_icon_content_description),
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.label_enter_reject_observations),
                        fontSize = Fonts.fontSizeNormal,
                        fontFamily = assistantFamily,
                    )
                    Spacer(modifier = Modifier.size(densityPixels48))
                }
                TextField(
                    value = observations,
                    onValueChange = { text ->
                        if (text.length <= Constants.MAX_CHARACTERS_BY_OBSERVATIONS) {
                            onObservationsChange(text)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = densityPixels154),
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.label_reject_observations_characters_max_field,
                                Constants.MAX_CHARACTERS_BY_OBSERVATIONS,
                            ),
                            fontSize = Fonts.fontSizeNormal,
                            fontFamily = assistantFamily,
                        )
                    },
                    supportingText = {
                        Text(
                            text = stringResource(
                                id = R.string.label_reject_observations_characters_left_field,
                                Constants.MAX_CHARACTERS_BY_OBSERVATIONS - observations.length,
                            ),
                            modifier = Modifier.padding(top = densityPixels4),
                        )
                    },
                    singleLine = false,
                    maxLines = Constants.MULTILINE,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        },
                    ),
                    shape = RoundedCornerShape(densityPixels8),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                )
                Button(
                    onClick = {
                        onClickSend.invoke()
                        onDismiss.invoke()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = densityPixels16),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = stringResource(id = R.string.send_icon_content_description),
                    )
                    Spacer(modifier = Modifier.width(densityPixels8))
                    Text(
                        text = stringResource(id = R.string.button_send_observations),
                        fontWeight = FontWeight.Bold,
                        fontFamily = assistantFamily,
                    )
                }
            }
        }
    }
}

@Composable
fun ActionTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(densityPixels8),
    contentPadding: PaddingValues = PaddingValues(
        top = densityPixels4,
        start = densityPixels4,
        end = densityPixels16,
        bottom = densityPixels4
    ),
    icon: Int,
    text: String,
    fontWeight: FontWeight = FontWeight.Bold,
    fontFamily: FontFamily,
    fontSize: TextUnit,
    iconBackgroundColor: Color = MaterialTheme.colorScheme.background,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    iconColor: Color = MaterialTheme.colorScheme.primary,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        contentPadding = contentPadding
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(densityPixels8)
        ) {
            Surface(
                modifier = Modifier.size(densityPixels40),
                shape = CircleShape,
                color = iconBackgroundColor
            ) {
                Icon(
                    modifier = Modifier
                        .padding(densityPixels8)
                        .size(Dimens.densityPixels32),
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(id = R.string.icon_content_description),
                    tint = iconColor
                )
            }
            Spacer(modifier = Modifier.padding(densityPixels4))
            Text(
                text = text,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                fontSize = fontSize,
                color = textColor
            )
        }
    }
}

@Composable
fun HeaderFileRow(text: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(id = R.string.leading_icon_content_description),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.width(densityPixels16))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            fontWeight = FontWeight.Normal,
            fontFamily = assistantFamily,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun FileSelectorField(
    context: Context,
    resultLauncher: ActivityResultLauncher<Intent>,
    headerText: String,
    headerIcon: ImageVector,
    onIconButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(densityPixels12),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HeaderFileRow(
            text = headerText,
            icon = headerIcon,
            modifier = Modifier.fillMaxWidth()
        )
        FilledTonalIconButton(onClick = onIconButtonClick) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(id = R.string.add_document_icon_content_description)
            )
        }
    }
}

//TODO: Refactor file item upload composable

@Composable
@Preview
fun FileItemForUpload(
    icon: Int = R.drawable.ic_file_type,
    name: String = "DocumentoProspecto2024.pdf",
) {
    Surface(
        modifier = Modifier
            .padding(densityPixels8),
        shape = RoundedCornerShape(densityPixels2),
        tonalElevation = densityPixels4
    ) {
        Column(
            modifier = Modifier.padding(densityPixels8),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.image_icon_content_description)
            )
            Spacer(modifier = Modifier.padding(vertical = densityPixels4))
            Text(
                modifier = Modifier.width(densityPixels80),
                text = name,
                fontSize = fontSizeSmall,
                fontFamily = assistantFamily,
                overflow = TextOverflow.Ellipsis,
                maxLines = TWO_LINES
            )
        }
    }
}

@Composable
@Preview
fun FileItemForDownload(
    icon: Int = R.drawable.ic_file_type,
    name: String = "DocumentoProspecto2024.pdf",
    size: String = "272 KB",
) {
    Surface(
        modifier = Modifier
            .width(densityPixels256)
            .height(densityPixels80)
            .padding(densityPixels8),
        shape = RoundedCornerShape(densityPixels2),
        tonalElevation = densityPixels4
    ) {
        Row(
            modifier = Modifier.padding(densityPixels8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.image_icon_content_description)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = densityPixels8),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = name,
                    fontSize = fontSizeExtraSmall,
                    fontFamily = assistantFamily,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = ONE_LINE
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = size,
                    fontSize = fontSizeExtraSmall,
                    fontFamily = assistantFamily,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
