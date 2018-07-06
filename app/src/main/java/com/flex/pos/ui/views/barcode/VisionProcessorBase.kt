package com.flex.pos.ui.views.barcode

import android.graphics.Bitmap
import android.media.Image
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

abstract class VisionProcessorBase<T> : VisionImageProcessor {

    private val shouldThrottle = AtomicBoolean(false)

    override fun process(data: ByteBuffer, frameMetadata: FrameMetadata) {

        if (shouldThrottle.get()) {
            return
        }

        val metadata = FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setWidth(frameMetadata.width)
                .setHeight(frameMetadata.height)
                .setRotation(frameMetadata.rotation)
                .build()

        detectInVisionImage(FirebaseVisionImage.fromByteBuffer(data, metadata), frameMetadata)

    }

    override fun process(bitmap: Bitmap) {
        if (shouldThrottle.get()) {
            return
        }

        detectInVisionImage(FirebaseVisionImage.fromBitmap(bitmap), null)
    }

    override fun process(bitmap: Image, rotation: Int) {
        if (shouldThrottle.get()) {
            return
        }

        val metadata = FrameMetadata(bitmap.width, bitmap.height)

        val vImage = FirebaseVisionImage.fromMediaImage(bitmap, rotation)

        detectInVisionImage(vImage, metadata)

    }

    private fun detectInVisionImage(image: FirebaseVisionImage, metadata: FrameMetadata?) {

        detectInImage(image)
                .addOnSuccessListener {
                    shouldThrottle.set(false)
                    this.onSuccess(it, metadata)
                }
                .addOnFailureListener {
                    shouldThrottle.set(false)
                    this.onFailure(it)
                }

        shouldThrottle.set(true)

    }

    override fun stop() {
    }

    protected abstract fun detectInImage(image: FirebaseVisionImage): Task<T>

    protected abstract fun onSuccess(results: T, frameMetadata: FrameMetadata?)

    protected abstract fun onFailure(e: Exception)

}