package org.thoughtcrime.securesms.prayers.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.components.settings.DSLSettingsFragment
import org.thoughtcrime.securesms.databinding.PrayersLandingFragmentBinding

class PrayersLandingFragment : Fragment() {

    private lateinit var binding: PrayersLandingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.prayers_landing_fragment, container, false)
        binding = PrayersLandingFragmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            prayersLandingFragmentCenterText.text = getString(R.string.ConversationListTabs__prayers)
        }
    }
}