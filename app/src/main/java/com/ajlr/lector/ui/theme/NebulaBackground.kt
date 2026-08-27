package com.ajlr.lector.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import com.ajlr.lector.R

@Composable
fun NebulaBackground(modifier: Modifier = Modifier) {
    val nebulaImage = ImageBitmap.imageResource(id = R.drawable.nebula_fondo)

    Canvas(modifier = modifier.fillMaxSize()) {
        drawImage(
            image = nebulaImage,
            dstSize = androidx.compose.ui.unit.IntSize(size.width.toInt(), size.height.toInt())
        )
    }
}