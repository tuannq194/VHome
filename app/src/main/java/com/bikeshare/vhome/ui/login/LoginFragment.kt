package com.bikeshare.vhome.ui.login

//import kotlinx.android.synthetic.main.fragment_login.view.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bikeshare.vhome.R
import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.data.model.LoginPost
import com.bikeshare.vhome.databinding.FragmentLoginBinding
import com.bikeshare.vhome.util.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    //private val viewModel: LoginViewModel by navGraphViewModels(R.id.main_navigation)
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            onLogin()
        }

        viewModel.loginResponseLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    is Resource.Success -> {
                        Log.d("LOGIN_OBSERVER", it.data!!.org_name)
                        lifecycleScope.launch {
                            viewModel.saveUserInformation(it.data.token, it.data.org_id, it.data.name)
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Login Failure", Toast.LENGTH_SHORT).show()
                        Log.e("LOGIN_OBSERVER_ERROR", it.message.toString())
                    }
                }
            }

        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }

    }

    private fun onLogin() {
        val identifier = binding.editTextPhone.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        if (identifier.isNotEmpty() && password.isNotEmpty()) {
            val userLoginPost = LoginPost(identifier, password)
            viewModel.login(userLoginPost)
        } else {
            Toast.makeText(requireContext(), "Hãy Điền Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}