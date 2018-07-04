package com.flex.pos.ui.views.barcode

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
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
import com.flex.pos.ui.utils.AlertUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_scanner_sheet.*
import kotlin.math.max
import kotlin.math.min

class FragmentScannerSheet : BottomSheetDialogFragment(), SurfaceHolder.Callback {

    private val RC_HANDLE_GMS = 9001

    private var cameraSource: CameraSource? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scanner_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        surfaceViewScanner.holder.addCallback(this)

        val barcodeFactory = BarcodeTrackerFactory(graphicOverlay as GraphicOverlay<BarcodeGraphic>) {
            Log.v("TAG", "Detected: ${it?.rawValue}")
        }

        val barcodeDetector = BarcodeDetector.Builder(context).build()

        barcodeDetector.setProcessor(MultiProcessor.Builder<Barcode>(barcodeFactory).build())

        if (!barcodeDetector.isOperational) {

            AlertUtil.showToast(context, "Barcode detection not yet available")

        }

        val facing = if (context!!.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            CameraSource.CAMERA_FACING_FRONT
        } else {
            CameraSource.CAMERA_FACING_BACK
        }

        cameraSource = CameraSource.Builder(context, barcodeDetector)
                .setFacing(facing)
                .build()

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

                val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity?.applicationContext)

                val msg: String? = when (code) {
                    ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, ConnectionResult.SERVICE_UPDATING -> {
                        "Google play service update required"
                    }

                    ConnectionResult.SERVICE_MISSING -> {
                        "Google play service missing"
                    }

                    ConnectionResult.SERVICE_DISABLED -> {
                        "Google play service disabled"
                    }

                    ConnectionResult.SERVICE_INVALID -> {
                        "Google play service error"
                    }

                    else -> null
                }

                if (msg != null) {
                    AlertUtil.showDialog(context!!, msg, R.string.cancel, {}, null)
                } else {
                    try {
                        cameraSource?.start(holder)

                        cameraSource?.also {
                            val min = min(it.previewSize.width, it.previewSize.height)
                            val max = max(it.previewSize.width, it.previewSize.height)
                            graphicOverlay.setCameraInfo(min, max, it.cameraFacing)
                            graphicOverlay.clear()
                        }
                    } catch (e: Exception) {
                        AlertUtil.showToast(context, "Unable to start camera")
                        cameraSource?.release()
                        cameraSource = null
                    }
                }

            }, 250)
        }
    }

}