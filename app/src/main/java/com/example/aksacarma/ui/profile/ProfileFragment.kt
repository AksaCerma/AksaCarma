package com.example.aksacarma.ui.profile

import android.content.Intent
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
import com.example.aksacarma.data.remote.response.UserData
import com.example.aksacarma.databinding.FragmentProfileBinding
import com.example.aksacarma.helper.reduceImageSize
import com.example.aksacarma.helper.uriToFile
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
        setupView()
        binding.apply {
            imageViewChangeAvatar.setOnClickListener { openGallery() }
            updateUser()
        }
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        return root
    }

    private fun setupView() {
        binding.toolbarProfile.imageViewSetting.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupUser() {
        profileViewModel.getUser().observe(viewLifecycleOwner) {
            setUserData(it.token)
        }
        profileViewModel.userResponse.observe(viewLifecycleOwner) {
            val userData = it.userData
            getUserData(userData)
        }
    }

    private fun getUserData(userData: UserData) {
        binding.apply {
           Glide.with(requireContext())
                .load(userData.avatarUrl)
                .error(R.drawable.outline_account_circle_24)
                .into(imageViewAvatar)
           textViewName.text = userData.name
           textViewUsername.text =  userData.username
        }
    }

    private val cropFragmentResultLauncher =
        registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val selectedImage = result.uriContent
                val localFile = selectedImage?.let { uriToFile(it, this.requireContext()) }

                getFile = localFile
                binding.imageViewAvatar.setImageURI(selectedImage)
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
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setUserData(token: String) {
        profileViewModel.getUserData(token)
    }

    private fun updateUser() {
        profileViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (getFile != null) {
                val file = reduceImageSize(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "avatar_image",
                    file.name,
                    requestImageFile
                )
                val token = user.token
                updateResponse(token, imageMultipart)
            }
        }
    }

    private fun updateResponse(token: String, avatar_image: MultipartBody.Part) {
        profileViewModel.updateUser(token, avatar_image)
        profileViewModel.updateUserResponse.observe(viewLifecycleOwner) {
            if (!it.error) {
                showToast()
            }
        }
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