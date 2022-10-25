package com.bikeshare.vhome.ui.listview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bikeshare.vhome.R
import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.databinding.FragmentListviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListViewFragment : Fragment() {
    private val viewModel: ListViewViewModel by viewModels()

    private var _binding: FragmentListviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListviewBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.qrscanFragment)

        }
        binding.cameraSo1.setOnClickListener {
            findNavController().navigate(R.id.liveviewFragment)
        }
    }
}