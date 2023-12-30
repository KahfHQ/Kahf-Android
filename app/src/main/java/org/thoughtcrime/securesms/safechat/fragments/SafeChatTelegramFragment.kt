package org.thoughtcrime.securesms.safechat.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.SafeChatTelegramFragmentBinding

class SafeChatTelegramFragment: Fragment() {

    private lateinit var binding: SafeChatTelegramFragmentBinding
    private val telegramWebUrl = "https://web.telegram.org/"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.safe_chat_telegram_fragment, container, false)
        binding = SafeChatTelegramFragmentBinding.bind(view)
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
            settings.domStorageEnabled = true

            // Allow zooming
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false

            // Load WhatsApp Web URL
            loadWhatsAppWeb()
        }
    }

    private fun loadWhatsAppWeb() {
        binding.apply {
            webView.loadUrl(telegramWebUrl)

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Page finished loading
                }
            }
        }
    }
}