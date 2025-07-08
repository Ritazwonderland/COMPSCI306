package com.example.a306simulation1.screen

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.a306simulation1.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CameraCaptureScreen : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnDiscard: Button
    private var photoBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_capture)

        imageView = findViewById(R.id.iv_captured_photo)
        btnSave = findViewById(R.id.btn_save)
        btnDiscard = findViewById(R.id.btn_discard)

        // Retrieve the captured photo
        photoBitmap = intent.getParcelableExtra("photo_data")
        imageView.setImageBitmap(photoBitmap)

        // Save button: Save to external storage and return to map
        btnSave.setOnClickListener {
            savePhotoToStorage()
            finish() // Return to MainActivity
        }

        // Discard button: Return to map without saving
        btnDiscard.setOnClickListener {
            finish()
        }
    }

    private fun savePhotoToStorage() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )

        try {
            val fos = FileOutputStream(imageFile)
            photoBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}