package br.com.antoniomonteiro.oni.saveeditor.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.antoniomonteiro.oni.saveeditor.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import saveeditor.composeapp.generated.resources.*

/**
 * A screen that prompts the user to upload a .sav file or load a demo.
 *
 * @param onFileSelect Callback triggered when the user wants to select a file.
 * @param onDemoSelect Callback triggered when the user wants to load a demo save.
 * @param error An optional error message to display.
 * @param modifier Modifier for this composable.
 */
@Composable
fun UploadScreen(
    onFileSelect: () -> Unit,
    onDemoSelect: () -> Unit,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp)
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 672.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                IconWithCircleBackground(
                    icon = Icons.Outlined.Description,
                    contentDescription = stringResource(Res.string.icon_save_file_description)
                )

                TitleAndDescription()

                if (error != null) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                ActionButtons(onFileSelect, onDemoSelect)

                SupportedFormatsFooter()
            }
        }
    }
}

@Composable
private fun IconWithCircleBackground(icon: ImageVector, contentDescription: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TitleAndDescription() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(Res.string.upload_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(Res.string.upload_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ActionButtons(onFileSelect: () -> Unit, onDemoSelect: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onFileSelect,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Icon(Icons.Outlined.UploadFile, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.button_select_file))
        }

        OrDivider()

        OutlinedButton(
            onClick = onDemoSelect,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text(stringResource(Res.string.button_load_demo), color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun OrDivider() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Text(
            text = stringResource(Res.string.or_divider),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SupportedFormatsFooter() {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
        Text(
            text = stringResource(Res.string.supported_formats),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
@Preview
private fun UploadScreenLightPreview() {
    AppTheme(darkTheme = false) {
        UploadScreen(onFileSelect = {}, onDemoSelect = {}, error = "This is an example error message.")
    }
}

@Composable
@Preview
private fun UploadScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        UploadScreen(onFileSelect = {}, onDemoSelect = {}, error = null)
    }
}
