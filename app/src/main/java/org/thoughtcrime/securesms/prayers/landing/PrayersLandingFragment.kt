package org.thoughtcrime.securesms.prayers.landing

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.PrayersLandingFragmentBinding
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants
import org.thoughtcrime.securesms.prayers.views.PrayerModel
import org.thoughtcrime.securesms.prayers.views.PrayerModelView

class PrayersLandingFragment : Fragment() {

    private lateinit var binding: PrayersLandingFragmentBinding
    private var prayerModels: List<PrayerModel>? = null
        set(value) {
            value?.let {
                field = value
                initViews()
            }
        }

    private var dateIndex = 1

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
        prepareData()
    }

    private fun prepareData() {
        val prayers = mutableListOf<PrayerModel>()
        prayers.add(PrayerModel("Fajr", "03:43 AM", PrayersConstants.NotificationTypes.MUTED))
        prayers.add(PrayerModel("Sunrise", "05:11 PM", PrayersConstants.NotificationTypes.VOICE_ENABLED))
        prayers.add(PrayerModel("Dhuhr", "12:04 PM", PrayersConstants.NotificationTypes.UNMUTED))
        prayers.add(PrayerModel("Asr", "03:18 PM", PrayersConstants.NotificationTypes.MUTED))
        prayers.add(PrayerModel("Magrib", "06:51 PM", PrayersConstants.NotificationTypes.UNMUTED))
        prayers.add(PrayerModel("Isha", "08:05 PM", PrayersConstants.NotificationTypes.UNMUTED))

        prayerModels = prayers.toList()
    }

    private fun initViews() {
        binding.apply {
            prayerModels?.forEach {
                val prayerModelView = PrayerModelView(requireContext(), it, this@PrayersLandingFragment)
                prayersLinearLayout.addView(prayerModelView)
            }

            goYesterdayBtn.setOnClickListener {
                if (dateIndex > 0) {
                    dateIndex--
                    prayerDayLabel.text = when (dateIndex) {
                        1 -> "Today"
                        else -> "Yesterday"
                    }
                    prayersLinearLayout.children.forEach {
                        val view = it as PrayerModelView?
                        if (dateIndex == 0)
                            view?.changeToYesterdayModel()
                    }
                }
            }

            goTomorrowBtn.setOnClickListener {
                if (dateIndex < 2) {
                    dateIndex++
                    prayerDayLabel.text = when (dateIndex) {
                        1 -> "Today"
                        else -> "Tomorrow"
                    }
                    prayersLinearLayout.children.forEach {
                        val view = it as PrayerModelView?
                        if (dateIndex > 0)
                            view?.changeToNormalModel()
                    }
                }
            }
        }
    }
}