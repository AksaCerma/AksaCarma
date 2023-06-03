package com.example.aksacarma.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.aksacarma.R
import com.example.aksacarma.databinding.FragmentProfileBinding
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.ui.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
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
        return root
    }

    private fun setupUser() {
//        showLoading()
        profileViewModel.getUser().observe(viewLifecycleOwner) {profileUser ->
            setProfileData(profileUser)
        }
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

    private fun setupAction() {
        binding.buttonLogout.setOnClickListener{
            profileViewModel.logoutUser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}