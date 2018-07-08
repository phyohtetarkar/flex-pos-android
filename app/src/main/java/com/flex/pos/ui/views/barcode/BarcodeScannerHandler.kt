package com.flex.pos.ui.views.barcode

import com.journeyapps.barcodescanner.BarcodeResult

interface BarcodeScannerHandler {

    fun onDetected(result: BarcodeResult?)

}