package com.project.timemanagerment.feature.profile.ui.moresetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.timemanagerment.databinding.FragmentMoreSettingBinding

class MoreSettingFragment : Fragment() {
    lateinit var binding: FragmentMoreSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        initListener()
        initClickNavigation()
    }

    private fun initClickNavigation() {
        binding.clUserAgreement.setOnClickListener {
            val directions = MoreSettingFragmentDirections.navToUserAgreementFragment()
            findNavController().navigate(directions)
        }
        binding.clPrivacyPolicy.setOnClickListener {
            val directions = MoreSettingFragmentDirections.navToPrivacyPolicyFragment()
            findNavController().navigate(directions)
        }

    }

    private fun initListener() {

    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}