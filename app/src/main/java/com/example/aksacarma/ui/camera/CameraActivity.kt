package com.example.aksacarma.ui.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.aksacarma.helper.createTempFile
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.aksacarma.databinding.ActivityCameraBinding
import com.example.aksacarma.helper.rotateImageIfRequired
import com.example.aksacarma.helper.uriToFile
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var photoPath: String
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            buttonCamera.setOnClickListener { startTakePhoto() }
            buttonGallery.setOnClickListener { openGallery() }
//            buttonUpload.setOnClickListener { uploadImage()}
        }
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage: Uri = result.data?.data as Uri
            val localFile = uriToFile(selectedImage, this)

            getFile = localFile
            binding.imageViewPost.setImageURI(selectedImage)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooseImage = Intent.createChooser(intent, "Pilih Gambar")
        launchGallery.launch(chooseImage)
    }

    private val launchCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val localFile = File(photoPath)
            getFile = localFile

            val photoResult = BitmapFactory.decodeFile(getFile?.path)
            val rotatedPhotoResult = rotateImageIfRequired(photoResult, getFile?.path)
            binding.imageViewPost.setImageBitmap(rotatedPhotoResult)
        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

       createTempFile(application).also {
           val photoURI: Uri = FileProvider.getUriForFile(
               this@CameraActivity, "com.example.aksacarma", it
           )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launchCamera.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this,"Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}