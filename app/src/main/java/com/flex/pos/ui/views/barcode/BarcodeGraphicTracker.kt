package com.flex.pos.ui.views.barcode

import android.support.annotation.UiThread
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

class BarcodeGraphicTracker(
        private val delegate: BarcodeGraphicTracker.BarcodeDetectorDelegate?
) : Tracker<Barcode>() {

    interface BarcodeDetectorDelegate {
        @UiThread
        fun onBarcodeDetected(b: Barcode?)
    }

    override fun onNewItem(id: Int, b: Barcode?) {
        delegate?.onBarcodeDetected(b)
    }

    override fun onUpdate(detections: Detector.Detections<Barcode>?, b: Barcode?) {
    }

    override fun onMissing(detections: Detector.Detections<Barcode>?) {
    }

    override fun onDone() {
    }

}