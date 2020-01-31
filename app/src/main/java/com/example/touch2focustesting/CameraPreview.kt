package com.example.touch2focustesting

import android.graphics.Rect
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.util.Log
import android.view.SurfaceHolder
import java.io.IOException
import java.util.*

class CameraPreview(width: Int, height: Int) : SurfaceHolder.Callback {
    private var mCamera: Camera? = null
    var params: Camera.Parameters? = null
    private var sHolder: SurfaceHolder? = null
    var supportedSizes: List<Camera.Size>? = null
    var isCamOpen = 0
    var isSizeSupported = false
    private val previewWidth: Int
    private val previewHeight: Int
    private fun openCamera(): Int {
        if (isCamOpen == 1) {
            releaseCamera()
        }
        mCamera =
            Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        if (mCamera == null) {
            return -1
        }
        params = mCamera?.parameters
        params?.setPreviewSize(previewWidth, previewHeight)
        try {
            mCamera?.parameters = params
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        mCamera?.startPreview()
        try {
            mCamera!!.setPreviewDisplay(sHolder)
        } catch (e: IOException) {
            mCamera!!.release()
            mCamera = null
            return -1
        }
        isCamOpen = 1
        return isCamOpen
    }

    fun releaseCamera() {
        if (mCamera != null) {
            mCamera!!.stopPreview()
            mCamera!!.setPreviewCallback(null)
            mCamera!!.release()
            mCamera = null
        }
        isCamOpen = 0
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        sHolder = holder
        isCamOpen = openCamera()
    }

    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int, width: Int,
        height: Int
    ) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        releaseCamera()
    }

    /**
     * Called from PreviewSurfaceView to set touch focus.
     *
     * @param - Rect - new area for auto focus
     */
    fun doTouchFocus(tfocusRect: Rect?) {
        Log.i(TAG, "TouchFocus")
        try {
            val focusList: MutableList<Camera.Area> =
                ArrayList()
            val focusArea =
                Camera.Area(tfocusRect, 1000)
            focusList.add(focusArea)
            val para = mCamera!!.parameters
            para.focusAreas = focusList
            para.meteringAreas = focusList
            mCamera!!.parameters = para
            mCamera!!.autoFocus(myAutoFocusCallback)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, "Unable to autofocus")
        }
    }

    /**
     * AutoFocus callback
     */
    var myAutoFocusCallback = AutoFocusCallback { arg0, arg1 ->
        if (arg0) {
            mCamera!!.cancelAutoFocus()
        }
    }

    companion object {
        private const val TAG = "CameraPreview"
    }

    init {
        Log.i("campreview", "Width = $width")
        Log.i("campreview", "Height = $height")
        previewWidth = width
        previewHeight = height
    }
}