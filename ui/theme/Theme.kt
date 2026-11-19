package com.nexora.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Paleta tomada de la imagen de referencia (azul marino + amarillo limón)
object NexoraColors {
    val Background = Color(0xFF0F1A3A)
    val Surface = Color(0xFF1B2850)
    val SurfaceHigh = Color(0xFF26345F)
    val Accent = Color(0xFFFDFF7E)
    val OnAccent = Color(0xFF14192B)
    val Income = Color(0xFF4CD27A)
    val Expense = Color(0xFFF0626E)
    val Warning = Color(0xFFF5D76E)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF9AA5C4)
}

private val scheme = darkColorScheme(
    primary = NexoraColors.Accent, onPrimary = NexoraColors.OnAccent,
    background = NexoraColors.Background, onBackground = NexoraColors.TextPrimary,
    surface = NexoraColors.Surface, onSurface = NexoraColors.TextPrimary,
    surfaceVariant = NexoraColors.SurfaceHigh, onSurfaceVariant = NexoraColors.TextSecondary,
    error = NexoraColors.Expense
)

@Composable
fun NexoraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = scheme,
        shapes = Shapes(small = RoundedCornerShape(12.dp), medium = RoundedCornerShape(18.dp), large = RoundedCornerShape(28.dp)),
        content = content
    )
}
