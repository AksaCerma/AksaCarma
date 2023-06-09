package com.example.aksacarma.ui.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canhub.cropper.*
import com.example.aksacarma.databinding.ActivityCameraBinding
import com.example.aksacarma.helper.reduceImageSize
import com.example.aksacarma.helper.uriToFile
import com.example.aksacarma.ui.ViewModelFactory
import com.example.aksacarma.ui.result.ResultActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import com.example.aksacarma.ui.main.MainActivity

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var getFile: File? = null
    private lateinit var factory: ViewModelFactory
    private val cameraViewModel: CameraViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        binding.apply {
            buttonGallery.setOnClickListener { openGallery() }
            buttonDetection.setOnClickListener { uploadImage()}
        }
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun setupView() {
       binding.toolbarDetection.imageViewBack.setOnClickListener{
           val intent = Intent(this@CameraActivity, MainActivity::class.java)
           startActivity(intent)
       }
    }

    private val cropActivityResultLauncher =
        registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val selectedImage = result.uriContent
                val localFile = selectedImage?.let { uriToFile(it, this) }

                getFile = localFile
                binding.imageViewPost.setImageURI(selectedImage)
            } else {
                Toast.makeText(this, "Gagal mengambil Gambar", Toast.LENGTH_SHORT).show()
            }
        }

    private fun openGallery() {
        cropActivityResultLauncher.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setAspectRatio(1,1)
                setFixAspectRatio(true)
            }
        )
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

    private fun uploadImage() {
        showLoading()
        cameraViewModel.getUser().observe(this@CameraActivity) { user ->
            if (getFile != null) {
                val file = reduceImageSize(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestImageFile
                )
                val token = user.token
                uploadResponse(token, imageMultipart)
            }
        }
    }

    private fun uploadResponse(token: String, image: MultipartBody.Part) {
        cameraViewModel.uploadImage(token, image)
        cameraViewModel.predictionResponse.observe(this@CameraActivity) {
            if (!it.error) {
                moveActivity()
            }
        }
        cameraViewModel.predictionResponse.observe(this@CameraActivity) {
            if (!it.error) {
                moveActivity()
            }
        }
        showToast()
    }


    private fun moveActivity() {
        val intent = Intent(this@CameraActivity, ResultActivity::class.java)
        intent.putExtra("photoPath", getFile?.path)
        startActivity(intent)
        finish()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun showToast() {
        cameraViewModel.textToast.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        cameraViewModel.isLoading.observe(this@CameraActivity) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

    }
}