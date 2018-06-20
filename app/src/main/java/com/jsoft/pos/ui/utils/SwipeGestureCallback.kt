package com.jsoft.pos.ui.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.jsoft.pos.R

class SwipeGestureCallback(
        private val context: Context,
        private val listener: OnSwipeDeleteListener
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val p = Paint()
    private val swpItems = mutableListOf<SwipeItem>()

    var gestureDetector: GestureDetector? = null

    interface OnSwipeDeleteListener {

        fun onDelete(position: Int)

        fun onCancel(position: Int)

    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            for (s in swpItems) {
                if (s.rectF.contains(e!!.x, e.y)) {
                    AlertUtil.showConfirmDelete(context, {
                        listener.onDelete(s.position)
                    }, {
                        listener.onCancel(s.position)
                    })
                    return true
                }
            }

            return false
        }

    }

    init {
        gestureDetector = GestureDetector(context, gestureListener)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return 0.5f.times(150)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val v = viewHolder.itemView

        val rect = RectF(v.right.toFloat().minus(v.height), v.top.toFloat(), v.right.toFloat(), v.bottom.toFloat())

        swpItems.add(SwipeItem(position, rect) {
            listener.onDelete(position)
        })
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        val icon: Bitmap

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            val itemView = viewHolder.itemView
            val position = viewHolder.adapterPosition

            val width = itemView.height / 3
            val tX = dX.times(itemView.height).div(itemView.width)

            p.color = Color.parseColor("#D13638")
            p.maskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.INNER)

            val background = RectF(itemView.right.toFloat() + tX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            c.drawRect(background, p)
            icon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getBitmapFromVector(context.resources.getDrawable(R.drawable.ic_delete_white, null))
            } else {
                getBitmapFromVector(context.resources.getDrawable(R.drawable.ic_delete_white))
            }

            val iconDest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
            c.drawBitmap(icon, null, iconDest, p)



            super.onChildDraw(c, recyclerView, viewHolder, tX, dY, actionState, isCurrentlyActive)

        }
    }

    override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val position = viewHolder?.adapterPosition

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (!isCurrentlyActive) {
                for (s in swpItems) {
                    if (s.position == position) {
                        swpItems.remove(s)
                        break
                    }
                }
            }
        }
    }

    private fun getBitmapFromVector(drawable: Drawable): Bitmap {

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    data class SwipeItem(
            var position: Int,
            var rectF: RectF,
            var handler: (Int) -> Unit
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SwipeItem

            if (position != other.position) return false

            return true
        }

        override fun hashCode(): Int {
            return position
        }
    }
}
