package com.algostack.smartcircuithouse.features.Authentication.login_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentLoginScreenBinding


class LoginScreen : Fragment() {

    var _binding: FragmentLoginScreenBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icClose.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_homeScreen)
        }
    }


}