package com.flex.pos.ui.views.barcode

import android.graphics.Bitmap
import android.media.Image
import java.nio.ByteBuffer

interface VisionImageProcessor {

    fun process(data: ByteBuffer, frameMetadata: FrameMetadata)

    fun process(bitmap: Bitmap)

    fun process(bitmap: Image, rotation: Int)

    fun stop()

}