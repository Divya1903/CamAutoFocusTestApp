package com.example.touch2focustesting

import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class TouchActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private var camView: PreviewSurfaceView? = null
    private var cameraPreview: CameraPreview? = null
    private var drawingView: DrawingView? = null
    private val previewWidth = 1280
    private val previewHeight = 720
    private lateinit var camHolder: SurfaceHolder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_touch)
        camView = findViewById<View>(R.id.preview_surface) as PreviewSurfaceView
        drawingView = findViewById<View>(R.id.drawing_surface) as DrawingView


        checkCameraPermissions()

    }

    private fun openCamera() {

        camHolder = camView!!.holder
        //cameraPreview.changeExposureComp(-currentAlphaAngle);
        cameraPreview = CameraPreview(previewWidth, previewHeight)
        camHolder.addCallback(cameraPreview)
        camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        camView!!.setListener(cameraPreview)
        camView!!.setDrawingView(drawingView)
        Log.d("Camera", "Opened")
    }

    @AfterPermissionGranted(REQ_CODE)
    private fun checkCameraPermissions() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            openCamera()
            Log.d("AllPermGranted", "true")
        } else {
            EasyPermissions.requestPermissions(
                this,
                "App requires camera access",
                REQ_CODE,
                Manifest.permission.CAMERA
            )
            Log.d("AllPermGranted", "false")
            Log.d("PermDialog", "requested")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d("Perm", perms[0] + "Denied")

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d("Perm", perms[0] + "Granted")
    }

    companion object {
        const val REQ_CODE = 400
    }

}