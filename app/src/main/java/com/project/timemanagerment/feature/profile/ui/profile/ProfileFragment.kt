package com.project.timemanagerment.feature.profile.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.timemanagerment.app.presentation.MainFragmentDirections
import com.project.timemanagerment.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    val viewModel: ProfileViewModel by viewModels()
    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subScribeUi()
        initClickWithNavigation()
    }

    private fun subScribeUi() {
        checkLoginStatusAndSetLoginUser()

    }

    private fun checkLoginStatusAndSetLoginUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            val user = viewModel.getUserPreferenceSync()

        }

    }

    private fun initClickWithNavigation() {
        //navigation to login or register
        binding.clUser.setOnClickListener {
            val direction = MainFragmentDirections.navToLoginFragment()
            findNavController().navigate(direction)
            /* val directions = MainFragmentDirections.navToUserSettingFragment()
             findNavController().navigate(directions)*/
        }
        binding.clFeedback.setOnClickListener {
            val directions = MainFragmentDirections.navToFeedbackFragment()
            findNavController().navigate(directions)
        }
        binding.clMoreSetting.setOnClickListener {
            val directions = MainFragmentDirections.navToMoreSettingFragment()
            findNavController().navigate(directions)
        }
        binding.clAbout.setOnClickListener {
            val directions = MainFragmentDirections.navToAboutFragment()
            findNavController().navigate(directions)
        }
    }
}