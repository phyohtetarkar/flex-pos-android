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
    private lateinit var iconDest: RectF
    private var swpPosition: Int? = null
    private val swpItems = mutableListOf<SwipeItem>()

    var gestureDetector: GestureDetector? = null

    interface OnSwipeDeleteListener {

        fun onDelete(position: Int)

        fun onCancel(position: Int)

    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
                if (iconDest.contains(e!!.x, e.y)) {
                    Log.v("TAG", "tapped!")
                   /* AlertUtil.showConfirmDelete(context, {
                        swpPosition?.also {
                            listener.onDelete(it)
                        }
                    }, {
                        swpPosition?.also {
                            listener.onCancel(it)
                        }
                    })*/
            }

            return true
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

        /*AlertUtil.showConfirmDelete(viewHolder.itemView.context, {
            listener.onDelete(position)
        }, {
            listener.onCancel(position)
        })*/

        swpItems.clear()

        swpItems.add(SwipeItem(position, RectF(150f, v.top.toFloat(), v.right.toFloat(), v.bottom.toFloat())))
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        val icon: Bitmap

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            val itemView = viewHolder.itemView
            val position = viewHolder.adapterPosition

            val height = itemView.bottom.toFloat() - itemView.top.toFloat()
            val width = height / 3
            val ctx = recyclerView.context

            p.color = Color.parseColor("#D13638")
            val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            c.drawRect(background, p)
            icon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getBitmapFromVector(ctx.resources.getDrawable(R.drawable.ic_delete_white, null))
            } else {
                getBitmapFromVector(ctx.resources.getDrawable(R.drawable.ic_delete_white_compat))
            }

            iconDest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
            c.drawBitmap(icon, null, iconDest, p)

            val translateX = dX.times(150).div(itemView.width)

            if (dX == 0.0f) {
                IntArray(swpItems.size) { it }.forEach {
                    val swipeItem = swpItems[it]
                    if (swipeItem.position == position) {
                        swpItems.removeAt(it)
                    }
                }
            }

            super.onChildDraw(c, recyclerView, viewHolder, translateX, dY, actionState, isCurrentlyActive)

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
            var rectF: RectF
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
