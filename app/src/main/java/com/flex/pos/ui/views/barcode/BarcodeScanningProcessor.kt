package com.flex.pos.ui.views.barcode

import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class BarcodeScanningProcessor : VisionProcessorBase<List<FirebaseVisionBarcode>>() {

    val detector: FirebaseVisionBarcodeDetector = FirebaseVision.getInstance().visionBarcodeDetector

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionBarcode>> {
        return detector.detectInImage(image)
    }

    override fun onSuccess(results: List<FirebaseVisionBarcode>, frameMetadata: FrameMetadata?) {

    }

    override fun onFailure(e: Exception) {

    }

    override fun stop() {
        detector.close()
    }

}