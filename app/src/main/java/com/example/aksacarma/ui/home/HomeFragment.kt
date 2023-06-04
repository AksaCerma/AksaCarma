package com.example.aksacarma.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.aksacarma.R
import com.example.aksacarma.databinding.FragmentHomeBinding
import com.example.aksacarma.databinding.FragmentProfileBinding
import com.example.aksacarma.model.UserModel
import com.example.aksacarma.ui.ViewModelFactory
import com.example.aksacarma.ui.profile.ProfileViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUser()
        return root
    }

    private fun setupUser() {
//        showLoading()
        homeViewModel.getUser().observe(viewLifecycleOwner) {
            setProfileData(it)
        }
    }

    private fun setProfileData(user: UserModel) {
        binding.apply {
            textViewInitial.text = user.name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}