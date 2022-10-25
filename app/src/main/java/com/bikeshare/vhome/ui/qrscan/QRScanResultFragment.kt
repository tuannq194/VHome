package com.bikeshare.vhome.ui.qrscan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bikeshare.vhome.R
import com.bikeshare.vhome.databinding.FragmentListviewBinding
import com.bikeshare.vhome.databinding.FragmentQRScanResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRScanResultFragment : Fragment() {
    private var _binding: FragmentQRScanResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQRScanResultBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

}