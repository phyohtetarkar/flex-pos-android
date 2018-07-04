package com.flex.pos.ui.views.barcode

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.google.android.gms.vision.barcode.Barcode

class BarcodeGraphic(overlay: GraphicOverlay<*>) : GraphicOverlay.Graphic(overlay) {

    var mId = 0
    var mBarcode: Barcode? = null

    private val COLOR_CHOICES = arrayOf(Color.BLUE, Color.CYAN, Color.GREEN)

    private var mCurrentColorIndex = 0

    private var mRectPaint: Paint
    private var mTextPaint: Paint

    init {
        mCurrentColorIndex = (mCurrentColorIndex + 1).rem(COLOR_CHOICES.size)
        val selectedColor = COLOR_CHOICES[mCurrentColorIndex]

        mRectPaint = Paint()
        mRectPaint.color = selectedColor
        mRectPaint.style = Paint.Style.STROKE
        mRectPaint.strokeWidth = 4.0f

        mTextPaint = Paint()
        mTextPaint.color = selectedColor
        mTextPaint.textSize = 14.0f

    }

    fun updateItem(barcode: Barcode?) {
        mBarcode = barcode
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        val barcode = mBarcode

        barcode?.also {
            val rect = RectF(it.boundingBox)
            rect.left = translateX(rect.left)
            rect.top = translateY(rect.top)
            rect.right = translateX(rect.right)
            rect.bottom = translateY(rect.bottom)

            canvas.drawRect(rect, mRectPaint)
            canvas.drawText(it.rawValue, rect.left, rect.bottom, mTextPaint)
        }

    }

}