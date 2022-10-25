package com.bikeshare.vhome.ui.qrscan

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bikeshare.vhome.R
import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.data.model.AttributesPost
import com.bikeshare.vhome.data.model.PreCheckPost
import com.bikeshare.vhome.databinding.FragmentQrscanBinding
import com.bikeshare.vhome.util.Resource
import com.budiyev.android.codescanner.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private const val CAMERA_REQUEST_CODE = 101

@AndroidEntryPoint
class QrscanFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner

    private val viewModel: QrscanViewModel by viewModels()

    private var _binding: FragmentQrscanBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrscanBinding.inflate(inflater, container, false)
        val view = binding.root

        setupPermissions()

        codeScanner = CodeScanner(requireActivity(), binding.scannerView)

        codeScanner.apply {
            // Parameters (default values)
            camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            isAutoFocusEnabled = true // Whether to enable auto focus or not
            isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            decodeCallback = DecodeCallback {
                getActivity()?.runOnUiThread {
                    onAttributes(it.text)
                }
            }
            errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                getActivity()?.runOnUiThread {
                    Toast.makeText(requireActivity(), "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        codeScanner.startPreview()
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.attributesResponseLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                when(it){
                    is Resource.Success -> {
                        Log.d("QRSCAN_SUCCESS_RES", it.data!!.devices.toString())
                        if (it.data!!.total == 0) {
                            lifecycleScope.launch {
                                //viewModel.saveCameraInfomation()
                                findNavController().navigate(R.id.QRScanResultFragment)
                            }

                        } else {
                            Toast.makeText(requireContext(), "Camera đã được thêm vào tài khoản khác", Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Scan Failure", Toast.LENGTH_LONG).show()
                        Log.e("QRSCAN_ERROR_RES", it.data.toString())
                    }
                }
            }
        })
    }

    private fun onAttributes(cameraInfo: String) {
        Toast.makeText(requireActivity(), "QRSCAN_RESULT: ${cameraInfo}", Toast.LENGTH_LONG).show()
        Log.d("QRSCAN_RESULT", cameraInfo)

        val userPreferences = UserPreferences(requireContext())
        val cameraSerial = cameraInfo.substring(13,25)
        val type: String = "AND"
        val queries = JsonArray()
        val `object` = JsonObject()
        `object`.addProperty("key", "device_serial")
        `object`.addProperty("value", cameraSerial)
        queries.add(`object`)

        val attributesPost = AttributesPost(type,queries)

        userPreferences.accessToken.asLiveData().observe(viewLifecycleOwner, Observer {
            //Toast.makeText(requireContext(),it ?: "Token is null", Toast.LENGTH_LONG).show()
            viewModel.attributes(it.toString(), attributesPost)
        })
    }

    private fun onPreCheck(cameraInfo: String) {
        val userPreferences = UserPreferences(requireContext())
        Log.d("QRSCAN_RESULT", cameraInfo)
        val cameraSerial: String = "CNME10000023"
        val cameraVerificationCode: String = "QRHT28"
        val preCheckPost = PreCheckPost(cameraSerial,cameraVerificationCode)

        //Lấy token "it" và gán vào header
        userPreferences.accessToken.asLiveData().observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(),it ?: "Token is null", Toast.LENGTH_LONG).show()
            viewModel.preCheck(it.toString(), preCheckPost)
        })
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireContext() as Activity,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireActivity(),
                        "You need the camera permission to be able to use this app!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                }
            }
        }
    }
}