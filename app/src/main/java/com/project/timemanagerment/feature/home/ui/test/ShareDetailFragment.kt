package com.project.timemanagerment.feature.home.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.project.timemanagerment.databinding.FragmentShareDetailBinding
import java.util.concurrent.TimeUnit

class ShareDetailFragment : Fragment() {
    companion object {
        const val TRANSITION_NAME_IMAGE = "image"
        const val TRANSITION_NAME_NAME = "name"
        const val TRANSITION_NAME_TOOLBAR = "toolbar"
        const val TRANSITION_NAME_BACKGROUND = "background"
        const val TRANSITION_NAME_FAVORITE = "favorite"
        const val TRANSITION_NAME_BOOKMARK = "bookmark"
        const val TRANSITION_NAME_SHARE = "share"
        const val TRANSITION_NAME_BODY = "body"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShareDetailBinding.inflate(inflater, container, false)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        //   sharedElementReturnTransition = MaterialContainerTransform(requireContext(), false)
        ViewCompat.setTransitionName(binding.ivView, ShareDetailFragment.TRANSITION_NAME_IMAGE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition(500L, TimeUnit.MILLISECONDS)
    }
}