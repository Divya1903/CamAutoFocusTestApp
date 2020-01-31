package com.example.touch2focustesting

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView

/**
 * SurfaceView to show LenxCameraPreview2 feed
 */
class PreviewSurfaceView(
    context: Context?,
    attrs: AttributeSet?
) : SurfaceView(context, attrs) {
    private var camPreview: CameraPreview? = null
    private var listenerSet = false
    var paint: Paint? = null
    private var drawingView: DrawingView? = null
    private var drawingViewSet = false
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!listenerSet) {
            return false
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            val touchRect = Rect(
                (x - 100).toInt(),
                (y - 100).toInt(),
                (x + 100).toInt(),
                (y + 100).toInt()
            )
            val targetFocusRect = Rect(
                touchRect.left * 2000 / this.width - 1000,
                touchRect.top * 2000 / this.height - 1000,
                touchRect.right * 2000 / this.width - 1000,
                touchRect.bottom * 2000 / this.height - 1000
            )
            camPreview!!.doTouchFocus(targetFocusRect)
            if (drawingViewSet) {
                drawingView!!.setHaveTouch(true, touchRect)
                drawingView!!.invalidate()
                // Remove the square after some time
                val handler = Handler()
                handler.postDelayed({
                    drawingView!!.setHaveTouch(false, Rect(0, 0, 0, 0))
                    drawingView!!.invalidate()
                }, 1000)
            }
        }
        return false
    }

    /**
     * set CameraPreview instance for touch focus.
     * @param camPreview - CameraPreview
     */
    fun setListener(camPreview: CameraPreview?) {
        this.camPreview = camPreview
        listenerSet = true
    }

    /**
     * set DrawingView instance for touch focus indication.
     * @param camPreview - DrawingView
     */
    fun setDrawingView(dView: DrawingView?) {
        drawingView = dView
        drawingViewSet = true
    }

    init {
        val paint = Paint()
        paint.color = Color.GREEN
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
    }
}