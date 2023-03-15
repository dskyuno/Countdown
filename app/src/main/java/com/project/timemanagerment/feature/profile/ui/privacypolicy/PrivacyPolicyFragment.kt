package com.project.timemanagerment.feature.profile.ui.privacypolicy

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.timemanagerment.R
import com.project.timemanagerment.databinding.FragmentPrivacyPolicyBinding


import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PrivacyPolicyFragment : Fragment() {
    lateinit var binding: FragmentPrivacyPolicyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(250)
            binding.webView.loadUrl("file:///android_asset/html/privacy_policy.html");
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress == 100) {
                        binding.progressBar.visibility = View.GONE;
                    } else {
                        binding.progressBar.visibility = View.VISIBLE;
                        binding.progressBar.progress = newProgress;
                    }
                }
            }

            binding.webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    val uname = resources.getString(R.string.app_name)
                    binding.webView.loadUrl("javascript:(function(){document.getElementById('appName').innerText = '$uname';})()")
                }
            }
        }
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}