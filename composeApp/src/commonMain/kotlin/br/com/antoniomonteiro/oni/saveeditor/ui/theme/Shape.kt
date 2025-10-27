package br.com.antoniomonteiro.oni.saveeditor.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Reduced corner radius for a sharper, more modern look, as requested.
val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(6.dp),
    large = RoundedCornerShape(8.dp) // Reduced from 10.dp to be less rounded
)
