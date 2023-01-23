package com.bikeshare.vhome.ui.camerahome.listView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bikeshare.vhome.R
import com.bikeshare.vhome.databinding.FragmentListviewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListViewFragment : Fragment() {
    private val viewModel: ListViewViewModel by viewModels()

    private var _binding: FragmentListviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var nav: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListviewBinding.inflate(inflater, container, false)
        val view = binding.root
        nav = requireActivity().findViewById(R.id.bottomNavigationView)
        nav.visibility = View.VISIBLE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAdd.setOnClickListener {
            nav.visibility = View.GONE
            findNavController().navigate(R.id.action_listviewFragment_to_qrscanFragment)

        }
        binding.cameraSo1.setOnClickListener {
            findNavController().navigate(R.id.action_listviewFragment_to_liveviewFragment)
        }

        /*requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }*/
    }
}