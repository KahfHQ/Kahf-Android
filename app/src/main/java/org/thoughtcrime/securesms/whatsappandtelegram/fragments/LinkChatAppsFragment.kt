package org.thoughtcrime.securesms.whatsappandtelegram.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.LinkChatAppsFragmentBinding

class LinkChatAppsFragment: Fragment() {

    private lateinit var binding: LinkChatAppsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.link_chat_apps_fragment, container, false)
        binding = LinkChatAppsFragmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

    }
}