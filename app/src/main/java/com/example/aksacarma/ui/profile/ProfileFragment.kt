package com.example.aksacarma.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aksacarma.databinding.FragmentProfileBinding
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

        setupAction()
        return root
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