package com.flex.pos.ui.views.barcode

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.vision.CameraSource
import java.util.*

class GraphicOverlay<T : GraphicOverlay.Graphic> : View {

    private val mLock = Any()
    private var mPreviewWidth = 0
    private var mPreviewHeight = 0
    private var mFacing = CameraSource.CAMERA_FACING_FRONT
    private var mGraphics = HashSet<T>()

    var mWidthScaleFactor = 1.0f
    var mHeightScaleFactor = 1.0f

    abstract class Graphic(private val mOverlay: GraphicOverlay<*>) {

        abstract fun draw(canvas: Canvas)

        fun scaleX(horizontal: Float): Float {
            return horizontal * mOverlay.mWidthScaleFactor
        }

        fun scaleY(vertical: Float): Float {
            return vertical * mOverlay.mHeightScaleFactor
        }

        fun translateX(x: Float): Float {
            return when (mOverlay.mFacing) {
                CameraSource.CAMERA_FACING_BACK -> mOverlay.width * scaleX(x)
                else -> scaleX(x)
            }
        }

        fun translateY(y: Float): Float {
            return scaleY(y)
        }

        fun postInvalidate() {
            mOverlay.postInvalidate()
        }

    }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun clear() {
        synchronized(mLock) {
            mGraphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic: T) {
        synchronized(mLock) {
            mGraphics.add(graphic)
        }
        postInvalidate()
    }

    fun remove(graphic: T) {
        synchronized(mLock) {
            mGraphics.remove(graphic)
        }
        postInvalidate()
    }

    fun getGraphics(): List<T> {
        synchronized(mLock) {
            return Vector(mGraphics)
        }
    }

    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
        synchronized(mLock) {
            mPreviewWidth = previewWidth
            mPreviewHeight = previewHeight
            mFacing = facing
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        synchronized(mLock) {
            canvas?.also { cv ->
                if((mPreviewWidth != 0) && (mPreviewHeight != 0)) {
                    mWidthScaleFactor = cv.width.toFloat() * mPreviewWidth
                    mHeightScaleFactor = cv.height.toFloat() * mPreviewHeight
                }

                mGraphics.forEach { it.draw(cv) }
            }

        }
    }

}