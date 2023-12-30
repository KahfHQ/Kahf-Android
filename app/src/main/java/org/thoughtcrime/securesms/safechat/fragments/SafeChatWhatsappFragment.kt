package org.thoughtcrime.securesms.safechat.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.SafeChatWhatsappFragmentBinding

class SafeChatWhatsappFragment: Fragment() {

    private lateinit var binding: SafeChatWhatsappFragmentBinding
    private val whatsAppWebUrl = "https://web.whatsapp.com/"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.safe_chat_whatsapp_fragment, container, false)
        binding = SafeChatWhatsappFragmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.userAgentString = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/60.0"
            settings.domStorageEnabled = true
            setInitialScale(100)

            // Allow zooming
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false

            // Load WhatsApp Web URL
            loadWhatsAppWeb()

            setOnKeyListener { _, keyCode, event ->
                 if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                     goBack()
                     return@setOnKeyListener true
                 }
                false
            }
        }
    }

    private fun loadWhatsAppWeb() {
        binding.apply {
            webView.loadUrl(whatsAppWebUrl)
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Page finished loading
                }
            }
        }
    }

    inner class CustomWebViewClient() : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            view?.loadUrl(whatsAppWebUrl)
            return true
        }


    }
}

