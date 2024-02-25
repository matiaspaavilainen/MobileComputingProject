package com.main.chatter

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedText(text: String, fontSize: Int) {
    // https://medium.com/androiddevelopers/animating-brush-text-coloring-in-compose-%EF%B8%8F-26ae99d9b402

    val currentFontSizePx = with(LocalDensity.current) { 80.sp.toPx() }
    val currentFontSizeDoublePx = currentFontSizePx * 2

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = currentFontSizeDoublePx,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing)),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6650A4),
            Color(0xFF7560AF),
            Color(0xFF8560B9),
            Color(0xFF9560C4),
            Color(0xFFA570CE),
            Color(0xFFB580D9),
            Color(0xFFC590E3),
            Color(0xFFD0BCFF)
        ),
        start = Offset(offset, offset),
        end = Offset(offset + currentFontSizePx, offset + currentFontSizePx),
        tileMode = TileMode.Mirror
    )

    Text(
        text = text, style = TextStyle(
            fontFamily = FontFamily.SansSerif, fontSize = fontSize.sp, brush = brush
        )
    )
}