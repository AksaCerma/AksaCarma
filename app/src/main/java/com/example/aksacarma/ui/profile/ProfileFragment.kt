package com.example.aksacarma.ui.profile

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.aksacarma.R
import com.example.aksacarma.databinding.FragmentProfileBinding
import com.example.aksacarma.helper.reduceImageSize
import com.example.aksacarma.helper.uriToFile
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.ui.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private var getFile: File? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUser()
        setupAction()
        binding.apply {
            imageViewChangeAvatar.setOnClickListener { openGallery() }
//            buttonUpdateUser.setOnClickListener { updateUser()}
        }
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        return root
    }

    private fun setupUser() {
        profileViewModel.getUser().observe(viewLifecycleOwner) {profileUser ->
            setProfileData(profileUser)
        }
    }

    private val cropFragmentResultLauncher =
        registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val selectedImage = result.uriContent
                val localFile = selectedImage?.let { uriToFile(it, this.requireContext()) }

                getFile = localFile
                binding.imageViewAvatar2.setImageURI(selectedImage)
            } else {
                Toast.makeText(this.requireContext(), "Gagal mengambil Gambar", Toast.LENGTH_SHORT).show()
            }
        }

    private fun openGallery() {
        cropFragmentResultLauncher.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setAspectRatio(1,1)
                setFixAspectRatio(true)
            }
        )
        updateUser()
    }


//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == ProfileFragment.REQUEST_CODE_PERMISSIONS) {
//            if (!allPermissionGranted()) {
//                Toast.makeText(this.requireActivity(),"Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
//    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setProfileData(user: UserModel) {
        binding.apply {
            Glide.with(requireContext())
                .load(user.avatar_url)
                .error(R.drawable.outline_account_circle_24)
                .into(imageViewAvatar)
            textViewUsername.text = user.username
            textViewName.text = user.name
        }
    }

    private fun updateUser() {
        profileViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (getFile != null) {
                val file = reduceImageSize(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestImageFile
                )
                val token = user.token
                updateResponse(token, imageMultipart)
            }
        }
    }

    private fun updateResponse(token: String, image: MultipartBody.Part) {
        profileViewModel.updateUser(token, image)
        profileViewModel.updateUserResponse.observe(viewLifecycleOwner) {
            if (!it.error) {
            }
        }
        showToast()
    }

    private fun setupAction() {
        binding.buttonLogout.setOnClickListener{
            profileViewModel.logoutUser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast() {
        profileViewModel.textToast.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

        companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

    }
}