// AppDrawer.kt
package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.alpha

@Composable
fun AppDrawer(
    current: Screen,
    enabled: Boolean,
    onNavigate: (Screen) -> Unit,
) {
    val disabledMod = Modifier.alpha(0.5f)

    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationDrawerItem(
            label = { Text("Colony") },
            selected = current == Screen.Colony,
            onClick = { if (enabled) onNavigate(Screen.Colony) },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            modifier = if (enabled) Modifier else disabledMod
        )
        NavigationDrawerItem(
            label = { Text("Duplicants") },
            selected = current == Screen.Duplicants,
            onClick = { if (enabled) onNavigate(Screen.Duplicants) },
            icon = { Icon(Icons.Filled.People, contentDescription = null) },
            modifier = if (enabled) Modifier else disabledMod
        )
    }
}
