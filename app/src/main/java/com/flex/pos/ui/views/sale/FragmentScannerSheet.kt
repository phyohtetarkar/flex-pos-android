package com.flex.pos.ui.views.sale

import android.Manifest.permission.CAMERA
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.PermissionChecker
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import com.flex.pos.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_scanner_sheet.*

class FragmentScannerSheet : BottomSheetDialogFragment(), SurfaceHolder.Callback {

    private var cameraSource: CameraSource? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scanner_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        surfaceViewScanner.holder.addCallback(this)

        val barcodeFactory = MultiProcessor.Factory<Barcode> {

            return@Factory object : Tracker<Barcode>() {

                override fun onUpdate(detections: Detector.Detections<Barcode>?, barcode: Barcode?) {
                    // TODO implementation
                    Log.v("TAG", barcode?.displayValue)
                }

            }
        }

        val barcodeDetector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        barcodeDetector.setProcessor(MultiProcessor.Builder<Barcode>(barcodeFactory).build())

        if (barcodeDetector.isOperational) {
            Log.v("TAG", "Detector Operational")
            cameraSource = CameraSource.Builder(context, barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(250, 100)
                    .build()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        surfaceViewScanner.holder.removeCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        cameraSource?.stop()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (PermissionChecker.checkSelfPermission(context!!, CAMERA) == PERMISSION_GRANTED) {
            Handler().postDelayed({
                cameraSource?.start(holder)
            }, 250)
        }
    }

}