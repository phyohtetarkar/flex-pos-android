package com.flex.pos.ui.views.barcode

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

class BarcodeTrackerFactory(
        private val delegate: BarcodeGraphicTracker.BarcodeDetectorDelegate?
) : MultiProcessor.Factory<Barcode> {

    override fun create(barcode: Barcode?): Tracker<Barcode> {
        return BarcodeGraphicTracker(delegate)
    }

}