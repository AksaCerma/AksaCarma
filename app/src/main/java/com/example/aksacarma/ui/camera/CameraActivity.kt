package com.example.aksacarma.ui.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.aksacarma.helper.createTempFile
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.aksacarma.databinding.ActivityCameraBinding
import com.example.aksacarma.helper.reduceImageSize
import com.example.aksacarma.helper.rotateImageIfRequired
import com.example.aksacarma.helper.uriToFile
import com.example.aksacarma.ui.ViewModelFactory
import com.example.aksacarma.ui.result.ResultActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var photoPath: String
    private var getFile: File? = null
    private lateinit var factory: ViewModelFactory
    private val cameraViewModel: CameraViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        binding.apply {
            buttonCamera.setOnClickListener { startTakePhoto() }
            buttonGallery.setOnClickListener { openGallery() }
            buttonDetection.setOnClickListener { uploadImage()}
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
                uploadResponse(token, imageMultipart,)
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


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}