package com.flex.pos.ui.views.barcode

import android.content.Context
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

class BarcodeTrackerFactory(
        private val mGraphicOverlay: GraphicOverlay<BarcodeGraphic>,
        private val delegate: Any?
) : MultiProcessor.Factory<Barcode> {

    override fun create(barcode: Barcode?): Tracker<Barcode> {
        val graphic = BarcodeGraphic(mGraphicOverlay)
        return BarcodeGraphicTracker(mGraphicOverlay, graphic, delegate)
    }

}