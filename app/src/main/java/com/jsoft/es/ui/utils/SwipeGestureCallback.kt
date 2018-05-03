package com.jsoft.es.ui.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.jsoft.es.R

class SwipeGestureCallback(private val listener: OnSwipeDeleteListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val p = Paint()

    interface OnSwipeDeleteListener {

        fun onDelete(position: Int)

        fun onCancel(position: Int)

    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        val builder = AlertDialog.Builder(viewHolder.itemView.context)
        builder.setMessage("Are you sure to delete?")
        builder.setPositiveButton("REMOVE") { _, _ -> listener.onDelete(position) }
        builder.setNegativeButton("CANCEL") { _, _ -> listener.onCancel(position) }
        builder.setCancelable(false)
        builder.show()
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        val icon: Bitmap

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView

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
            val iconDest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
            c.drawBitmap(icon, null, iconDest, p)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        }
    }

    private fun getBitmapFromVector(drawable: Drawable): Bitmap {

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
