package com.flex.pos.ui.views.barcode

import android.util.Log
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

class BarcodeGraphicTracker(
        private val mOverlay: GraphicOverlay<BarcodeGraphic>,
        private val mGraphic: BarcodeGraphic,
        private val onBarcodeDetected: (Barcode?) -> Unit
) : Tracker<Barcode>() {

    override fun onNewItem(id: Int, b: Barcode?) {
        mGraphic.mId = id
        onBarcodeDetected(b)
    }

    override fun onUpdate(detections: Detector.Detections<Barcode>?, b: Barcode?) {
        mOverlay.add(mGraphic)
        mGraphic.updateItem(b)
    }

    override fun onMissing(detections: Detector.Detections<Barcode>?) {
        mOverlay.remove(mGraphic)
    }

    override fun onDone() {
        mOverlay.remove(mGraphic)
    }

}