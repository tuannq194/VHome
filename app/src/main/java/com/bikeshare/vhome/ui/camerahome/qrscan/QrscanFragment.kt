package com.bikeshare.vhome.ui.camerahome.qrscan

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bikeshare.vhome.R
import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.data.model.AttributesPost
import com.bikeshare.vhome.databinding.FragmentQrscanBinding
import com.bikeshare.vhome.util.Resource
import com.budiyev.android.codescanner.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val CAMERA_REQUEST_CODE = 101

@AndroidEntryPoint
class QrscanFragment : Fragment() {
    private val viewModel: QrscanViewModel by viewModels()

    private var _binding: FragmentQrscanBinding? = null
    private val binding get() = _binding!!

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrscanBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Kiểm tra quyền để quét QR bằng Camera*/
        checkPermissions()

        /** Lắng nghe response Attributes Camera */
        viewModel.attributesResponseLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    is Resource.Success -> {
                        Log.d("QRSCAN_OBSERVER", it.data!!.devices.toString())
                        if (it.data.total == 0) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                findNavController().navigate(R.id.action_qrscanFragment_to_QRScanResultFragment)
                            }
                        } else {
                            Toast.makeText(requireContext(), "Camera đã được thêm vào tài khoản khác", Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Scan Failure", Toast.LENGTH_LONG).show()
                        Log.e("QRSCAN_OBSERVER_ERROR", it.data.toString())
                    }
                }
            }
        })
    }

    /** Kiểm tra quyền ứng dụng */
    private fun checkPermissions(){
        val rxPermissions = RxPermissions(this)

        rxPermissions
            .request(
                Manifest.permission.CAMERA
            )
            .subscribe { granted ->
                if (granted) {
                    scanQR()
                } else {
                    Toast.makeText(requireContext(),"Please Allow App To Access Camera Then Retry",Toast.LENGTH_SHORT).show()
                }
            }
    }

    /** Quét mã QR*/
    private fun scanQR(){
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
                    Toast.makeText(
                        requireActivity(),
                        "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        codeScanner.startPreview()
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    /** Kiểm tra Camera đã được thêm vào tài khoản chưa */
    private fun onAttributes(cameraInfo: String) {
        Toast.makeText(requireActivity(), "QRSCAN_RESULT: ${cameraInfo}", Toast.LENGTH_LONG).show()

        val userPreferences = UserPreferences(requireContext())

        val splits = cameraInfo.split(" ").toTypedArray()
        val cameraManufacturer = splits[0]
        val cameraModel = splits[1]
        val cameraSerial = splits[2]
        val cameraVerificationCode = splits[3]
        Log.d("QRSCAN_RESULT", cameraManufacturer + " " + cameraModel + " " + cameraSerial + " " + cameraVerificationCode)

        viewModel.saveCameraInformation(cameraManufacturer, cameraModel, cameraSerial, cameraVerificationCode)

        val type = "AND"
        val queries = JsonArray()
        val `object` = JsonObject()
        `object`.addProperty("key", "device_serial")
        `object`.addProperty("value", cameraSerial)
        queries.add(`object`)

        val attributesPost = AttributesPost(type, queries)

        userPreferences.accessToken.asLiveData().observe(viewLifecycleOwner, Observer {
            viewModel.attributes(it.toString(), attributesPost)
        })
    }

    /** Kiểm tra quyền ứng dụng cách cũ*/
    /*private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
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
    }*/
}