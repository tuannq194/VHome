package com.bikeshare.vhome.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bikeshare.vhome.R
import com.bikeshare.vhome.data.UserPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashFragment : Fragment() {
    private lateinit var nav: BottomNavigationView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("SPLASH FRAGMENT","onCreateView")
        // Inflate the layout for this fragment
        nav = requireActivity().findViewById(R.id.bottomNavigationView)
        nav.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            UserPreferences(requireContext()).accessToken.asLiveData().observe(viewLifecycleOwner, Observer {
                if (it!=null){
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                } else{
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            })
        }, 500)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}