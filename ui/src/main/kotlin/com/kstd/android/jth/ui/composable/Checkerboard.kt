package com.kstd.android.jth.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A Composable that draws a checkerboard pattern using theme colors.
 * This is a resolution-independent and theme-aware alternative to using a tiled bitmap.
 */
@Composable
fun CheckerboardBackground(
    modifier: Modifier = Modifier,
    squareSize: Dp = 16.dp
) {
    val squareSizePx = with(LocalDensity.current) { squareSize.toPx() }
    val color1 = MaterialTheme.colorScheme.surfaceVariant
    val color2 = MaterialTheme.colorScheme.surface

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val horizontalSquares = (width / squareSizePx).toInt() + 1
        val verticalSquares = (height / squareSizePx).toInt() + 1

        for (i in 0 until verticalSquares) {
            for (j in 0 until horizontalSquares) {
                val color = if ((i + j) % 2 == 0) color1 else color2
                drawRect(
                    color = color,
                    topLeft = Offset(j * squareSizePx, i * squareSizePx),
                    size = Size(squareSizePx, squareSizePx)
                )
            }
        }
    }
}
