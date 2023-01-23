package com.bikeshare.vhome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bikeshare.vhome.R
import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.databinding.FragmentHomeBinding
import com.bikeshare.vhome.ui.camerahome.connectDevice.ConnectDeviceFragmentArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var nav: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val userPreferences = UserPreferences(requireContext())
        lifecycleScope.launch {
            binding.tvHello.setText("Xin chào, ${userPreferences.accessUsernameString()}")
        }
        nav = requireActivity().findViewById(R.id.bottomNavigationView)
        nav.visibility = View.GONE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonHomeCamera.setOnClickListener {
            lifecycleScope.launch {
                findNavController().navigate(R.id.action_homeFragment_to_camera_home_nav)
               //nav.visibility = View.VISIBLE
            }
        }
        binding.buttonLogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clearData()
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }
    }
}