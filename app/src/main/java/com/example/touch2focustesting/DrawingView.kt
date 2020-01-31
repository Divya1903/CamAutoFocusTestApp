package com.example.touch2focustesting

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * Extends View. Just used to draw Rect when the screen is touched
 * for auto focus.
 *
 * Use setHaveTouch function to set the status and the Rect to be drawn.
 * Call invalidate to draw Rect. Call invalidate again after
 * setHaveTouch(false, Rect(0, 0, 0, 0)) to hide the rectangle.
 */
class DrawingView(
    context: Context?,
    attrs: AttributeSet?
) : View(context, attrs) {
    private var haveTouch = false
    private var touchArea: Rect? = null
    private val paint: Paint
    fun setHaveTouch(`val`: Boolean, rect: Rect?) {
        haveTouch = `val`
        touchArea = rect
    }

    public override fun onDraw(canvas: Canvas) {
        if (haveTouch) { //drawingPaint.setColor(Color.BLUE);
            canvas.drawRect(
                touchArea!!.left.toFloat(),
                touchArea!!.top.toFloat(),
                touchArea!!.right.toFloat(),
                touchArea!!.bottom.toFloat(),
                paint
            )
        }
    }

    init {
        paint = Paint()
        paint.color = -0x11282829
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        haveTouch = false
    }
}