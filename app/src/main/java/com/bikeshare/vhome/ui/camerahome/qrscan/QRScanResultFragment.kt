package com.bikeshare.vhome.ui.camerahome.qrscan

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bikeshare.vhome.R
import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.data.model.PreCheckPost
import com.bikeshare.vhome.databinding.FragmentQRScanResultBinding
import com.bikeshare.vhome.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class QRScanResultFragment : Fragment() {
    private val viewModel: QRScanResultViewModel by viewModels()

    private var _binding: FragmentQRScanResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQRScanResultBinding.inflate(inflater, container, false)
        val view = binding.root

        val userPreferences = UserPreferences(requireContext())

        userPreferences.accessCameraSerial.asLiveData().observe(viewLifecycleOwner, Observer{
            binding.textViewSerial.setText("Đã tìm thấy thiết bị có mã serial\n${it.toString()}")
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonContinue.setOnClickListener{
            onPreCheck()
        }
        /** Lắng nghe response PreCheck Camera */
        viewModel.preCheckResponseLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                when(it){
                    is Resource.Success ->{
                        val response: String = it.data?.data.toString()
                        val obj = JSONObject(response)
                        Log.d("QRRESULT_OBSERVER", "Camera Status: ${obj.getString("connectionStatus")}")
                        if (obj.getInt("connectionStatus")==1){
                            lifecycleScope.launch(Dispatchers.Main) {
                                findNavController().navigate(R.id.action_QRScanResultFragment_to_connectDeviceWifiFragment)
                            }
                        }else if(obj.getInt("connectionStatus")==0){
                            Toast.makeText(requireContext(), "Camera Đang Online, Hãy Reset Camera", Toast.LENGTH_SHORT).show()
                        }else{
                            Log.d("QRPRECHECK_OBSERVER", "ERROR")
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "QR Scan Error", Toast.LENGTH_LONG).show()
                        Log.e("QRPRECHECK_OBSERVER_ERROR", it.message.toString())
                    }
                }
            }
        })
    }

    /** Kiểm tra trạng thái Online/Offline của Camera */
    private fun onPreCheck() {
        val userPreferences = UserPreferences(requireContext())

        userPreferences.accessToken.asLiveData().observe(viewLifecycleOwner, Observer {
            val token = it.toString()
            userPreferences.accessCameraSerial.asLiveData().observe(viewLifecycleOwner, Observer {
                val cameraSerial = it.toString()
                userPreferences.accessCameraVerificationCode.asLiveData().observe(viewLifecycleOwner, Observer {
                    val cameraVerificationCode = it.toString()
                    val preCheckPost = PreCheckPost(cameraSerial, cameraVerificationCode)
                    viewModel.preCheck(token, preCheckPost)
                })
            })
        })
    }

}