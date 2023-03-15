package com.project.timemanagerment.feature.home.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.Fade
import com.project.timemanagerment.databinding.FragmentShareItemBinding
import com.project.timemanagerment.feature.home.ui.countdowndetail.FAST_OUT_LINEAR_IN
import com.project.timemanagerment.feature.home.ui.countdowndetail.LINEAR_OUT_SLOW_IN

class ShareItemFragment : Fragment() {
    companion object {
        const val MEDIUM_EXPAND_DURATION = 250L
        const val MEDIUM_COLLAPSE_DURATION = 200L

        const val LARGE_EXPAND_DURATION = 300L
        const val LARGE_COLLAPSE_DURATION = 250L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShareItemBinding.inflate(inflater, container, false)

        val extras =
            FragmentNavigatorExtras(binding.ivView to ShareDetailFragment.TRANSITION_NAME_IMAGE)
        ViewCompat.setTransitionName(binding.ivView, ShareDetailFragment.TRANSITION_NAME_IMAGE)

        exitTransition = Fade(Fade.OUT).apply {
            duration = LARGE_EXPAND_DURATION / 2

            interpolator = FAST_OUT_LINEAR_IN
        }
        exitTransition = Fade(Fade.IN).apply {
            duration = LARGE_COLLAPSE_DURATION / 2
            startDelay = LARGE_COLLAPSE_DURATION / 2

            interpolator = LINEAR_OUT_SLOW_IN
        }


        return binding.root
    }
}