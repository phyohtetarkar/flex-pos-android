package com.jsoft.pos.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatRadioButton
import android.util.AttributeSet
import android.view.View
import com.jsoft.pos.R

class RoundedView : View {

    private val paint = Paint()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val hex = contentDescription ?: return

        if (width == 0 || height == 0) {
            return
        }

        val radius = width / 2

        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = Color.parseColor(hex.toString())

        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint)

    }

}

class RoundedImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return

        if (width == 0 || height == 0) {
            return
        }
        val b = (drawable as BitmapDrawable).bitmap
        var bitmap: Bitmap? = null
        if (b != null) {
            bitmap = b.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            val bitmapDrawable: BitmapDrawable? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                resources.getDrawable(R.drawable.ic_placeholder, null) as BitmapDrawable
            } else {
                resources.getDrawable(R.drawable.ic_placeholder) as BitmapDrawable
            }
            if (bitmapDrawable != null) {
                bitmap = bitmapDrawable.bitmap
            }
        }

        val w = width
        val roundBitmap = getCroppedBitmap(bitmap!!, w)
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)
    }

    private fun getCroppedBitmap(bmp: Bitmap, radius: Int): Bitmap {
        val sBmp: Bitmap

        if (bmp.width != radius || bmp.height != radius) {
            val smallest = Math.min(bmp.width, bmp.height).toFloat()
            val factor = smallest / radius
            sBmp = Bitmap.createScaledBitmap(bmp, (bmp.width / factor).toInt(), (bmp.height / factor).toInt(), false)
        } else {
            sBmp = bmp
        }

        val output = Bitmap.createBitmap(radius, radius,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, radius, radius)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = Color.parseColor("#BAB399")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0f, 0f, radius.toFloat(), radius.toFloat(), 10f, 10f, paint)
        } else {
            canvas.drawRoundRect(RectF(0f, 0f, radius.toFloat(), radius.toFloat()), 10f, 10f, paint)
        }
        /*canvas.drawCircle(radius / 2,
                radius / 2, radius / 2, paint);*/
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(sBmp, rect, rect, paint)

        return output
    }
}

class SquareRadioButton : AppCompatRadioButton {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}

