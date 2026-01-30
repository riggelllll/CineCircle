package com.koniukhov.cinecirclex.feature.lists.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.koniukhov.cinecirclex.feature.collections.R

class SwipeToDeleteCallback(
    context: Context,
    private val onSwipeToDelete: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val backgroundColor = ContextCompat.getColor(context, android.R.color.holo_red_dark)
    private val background = backgroundColor.toDrawable()
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    private val maxSwipeDistance = context.resources.displayMetrics.density * 80

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipeToDelete(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        val clampedDX = if (dX < -maxSwipeDistance) -maxSwipeDistance else dX

        background.setBounds(
            itemView.right + clampedDX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        deleteIcon?.let { icon ->
            val iconMargin = (itemHeight - icon.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemHeight - icon.intrinsicHeight) / 2
            val iconBottom = iconTop + icon.intrinsicHeight
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, clampedDX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 0.1f
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return defaultValue * 0.5f
    }
}