package com.project.timemanagerment.feature.profile.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.timemanagerment.databinding.FragmentAboutBinding
import com.project.timemanagerment.feature.profile.ui.login.LoginFragmentDirections

class AboutFragment : Fragment() {
    lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setClickNavigation()
    }

    private fun setClickNavigation() {
        binding.llUserAgreement.setOnClickListener {
            val directions = LoginFragmentDirections.navToUserAgreementFragment()
            findNavController().navigate(directions)
        }
        binding.llPrivacyPolicy.setOnClickListener {
            val directions = LoginFragmentDirections.navToPrivacyPolicyFragment()
            findNavController().navigate(directions)
        }
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}