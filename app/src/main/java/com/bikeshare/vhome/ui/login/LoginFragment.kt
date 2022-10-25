package com.bikeshare.vhome.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bikeshare.vhome.R
import com.bikeshare.vhome.data.model.LoginPost
import com.bikeshare.vhome.databinding.FragmentLoginBinding
import com.bikeshare.vhome.util.Resource
import com.bikeshare.vhome.util.enable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()

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

        viewModel.loginResponseLiveData.observe(viewLifecycleOwner, Observer{
            it.getContentIfNotHandled()?.let {
                when (it){
                    is Resource.Success -> {
                        Log.d("LOGIN_FRAGMENT_OBSERVER", it.data!!.org_name)
                        lifecycleScope.launch {
                            viewModel.saveAccessTokens(it.data!!.token)
                            findNavController().navigate(R.id.listviewFragment)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Login Failure", Toast.LENGTH_LONG).show()
                        Log.e("LOGIN_ERROR_RES", it.message.toString())
                    }
                }
            }

        })

        binding.editTextPassword.addTextChangedListener {
            val identifier = binding.editTextPhone.text.toString().trim()
            binding.buttonLogin.enable(identifier.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.buttonLogin.setOnClickListener {
            onLogin()
        }
    }

    private fun onLogin() {
        val identifier = binding.editTextPhone.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val userLoginPost = LoginPost(identifier, password)
        viewModel.login(userLoginPost)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}