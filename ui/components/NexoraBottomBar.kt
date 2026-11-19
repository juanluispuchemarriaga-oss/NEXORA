package com.nexora.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nexora.navigation.Destination
import com.nexora.ui.theme.NexoraColors

/** Barra en píldora: icono activo en círculo blanco + botón + amarillo, como la referencia. */
@Composable
fun NexoraBottomBar(
    current: String?, onSelect: (Destination) -> Unit, onAdd: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier
            .navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp)
            .shadow(16.dp, CircleShape)
            .clip(CircleShape).background(NexoraColors.Surface).padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Destination.entries.forEach { d ->
            val selected = current == d.route
            val bg by animateColorAsState(if (selected) NexoraColors.TextPrimary else NexoraColors.SurfaceHigh, label = "bg")
            Box(
                Modifier.size(46.dp).clip(CircleShape).background(bg).clickable(role = Role.Tab) { onSelect(d) },
                contentAlignment = Alignment.Center
            ) {
                Icon(d.icon, d.label, tint = if (selected) NexoraColors.Background else NexoraColors.TextPrimary, modifier = Modifier.size(22.dp))
            }
        }
        Box(
            Modifier.size(46.dp).clip(CircleShape).background(NexoraColors.Accent).clickable(role = Role.Button, onClick = onAdd),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Add, "Nuevo movimiento", tint = NexoraColors.OnAccent) }
    }
}
