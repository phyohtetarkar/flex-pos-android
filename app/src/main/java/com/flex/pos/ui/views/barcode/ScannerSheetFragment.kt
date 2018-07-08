package com.flex.pos.ui.views.barcode

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flex.pos.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.fragment_scanner_sheet_2.*

class ScannerSheetFragment : BottomSheetDialogFragment() {

    private val TAG = "ScannerSheetFragment"

    private var barcodeScannerHandler: BarcodeScannerHandler? = null
    private var mediaPlayer: MediaPlayer? = null

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            Log.i(TAG, result?.text)
            playBeepSound()
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scanner_sheet_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formats = mutableListOf(
                BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.EAN_8,
                BarcodeFormat.EAN_13,
                BarcodeFormat.CODE_93,
                BarcodeFormat.CODE_39,
                BarcodeFormat.CODE_128,
                BarcodeFormat.CODABAR,
                BarcodeFormat.ITF,
                BarcodeFormat.RSS_14,
                BarcodeFormat.RSS_EXPANDED,
                BarcodeFormat.DATA_MATRIX,
                BarcodeFormat.MAXICODE
        )

        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.decodeContinuous(callback)
        barcodeView.setStatusText("")
        barcodeView.setTorchOn()


    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        barcodeScannerHandler = null
    }

    fun pause() {
        barcodeView.pause()
    }

    fun resume() {
        barcodeView.resume()
    }

    private fun playBeepSound() {

        mediaPlayer?.also {
            if (it.isPlaying) {
                it.stop()
            }
            it.reset()
            it.release()
        }

        context?.getResources()?.openRawResourceFd(R.raw.barcode_read_effect)?.also {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_NOTIFICATION)
            mediaPlayer?.setDataSource(it.fileDescriptor, it.startOffset, it.length)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        }

    }

}