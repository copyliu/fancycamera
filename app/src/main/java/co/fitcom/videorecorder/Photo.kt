package co.fitcom.videorecorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.widget.ImageView

import co.fitcom.fancycamera.CameraEventListenerUI
import co.fitcom.fancycamera.EventType
import co.fitcom.fancycamera.FancyCamera
import co.fitcom.fancycamera.Event

class Photo : AppCompatActivity() {
    internal lateinit var cameraView: FancyCamera
    internal lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        imageView = findViewById(R.id.imageView)
        cameraView = findViewById(R.id.PhotoView)
        cameraView.quality = FancyCamera.Quality.HIGHEST
        cameraView.setListener(object : CameraEventListenerUI() {
            override fun onCameraOpenUI() {

            }

            override fun onCameraCloseUI() {

            }

            override fun onEventUI(event: Event) {
                if (event.type === EventType.Photo && event.file != null) {
                    imageView.setImageURI(Uri.fromFile(event.file!!))
                } else {
                    println(event.message)
                }
            }

        })
        cameraView.saveToGallery = true
         cameraView.autoSquareCrop = true
    }

    fun takePhoto(view: View) {
        if (cameraView.hasStoragePermission()) {
            cameraView.takePhoto()
        } else {
            cameraView.requestStoragePermission()
        }
    }

    fun toggleFlash(view: View) {
        cameraView.toggleFlash()
    }

    fun toggleCamera(view: View) {
        cameraView.toggleCamera()
    }

    fun goToHome(view: View) {
        val i = Intent(this, Home::class.java)
        startActivity(i)
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var count = 0
        for (grant in grantResults) {
            if (permissions[count] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grant == PackageManager.PERMISSION_GRANTED) {
                cameraView.takePhoto()
                break
            }
            count++
        }
    }

}
