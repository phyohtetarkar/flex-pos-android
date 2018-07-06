package com.flex.pos.ui.views.barcode

import com.google.android.gms.vision.CameraSource

data class FrameMetadata(
        val width: Int,
        val height: Int,
        val rotation: Int = 90,
        val cameraFacing: Int = CameraSource.CAMERA_FACING_BACK
)