package org.thoughtcrime.securesms.prayers.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.AdhanAndNavigationBottomSheetFragmentBinding
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants.NotificationTypes
import org.thoughtcrime.securesms.prayers.listeners.OnNotificationTypeSelectedListener

class AdhanAndNotificationBottomSheetFragment constructor(private val notificationType: String,
                                                          private val onNotificationTypeSelectedListener: OnNotificationTypeSelectedListener): BottomSheetDialogFragment() {

    private lateinit var binding: AdhanAndNavigationBottomSheetFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.adhan_and_navigation_bottom_sheet_fragment, container, false)
        binding = AdhanAndNavigationBottomSheetFragmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrangeCells()
    }

    private fun arrangeCells() {
        binding.apply {
            when (notificationType) {
                NotificationTypes.MUTED -> {
                    silent.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_selected_background)
                    notification.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_unselected_background)
                    adhan.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_unselected_background)

                    silentIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_selected_icon_blue))
                    notificationIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                    adhanIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))

                    silentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_selected_icon_blue))
                    notificationText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                    adhanText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                }
                NotificationTypes.UNMUTED -> {
                    silent.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_unselected_background)
                    notification.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_selected_background)
                    adhan.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_unselected_background)

                    silentIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                    notificationIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_selected_icon_blue))
                    adhanIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))

                    silentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                    notificationText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_selected_icon_blue))
                    adhanText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                }
                NotificationTypes.VOICE_ENABLED -> {
                    silent.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_unselected_background)
                    notification.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_unselected_background)
                    adhan.background = ContextCompat.getDrawable(requireContext(), R.drawable.adhan_and_notification_selected_background)

                    silentIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                    notificationIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                    adhanIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.kahf_selected_icon_blue))

                    silentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                    notificationText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_deselected_icon_gray))
                    adhanText.setTextColor(ContextCompat.getColor(requireContext(), R.color.kahf_selected_icon_blue))
                }
            }

            silent.setOnClickListener {
                onNotificationTypeSelectedListener.onNotificationTypeSelected(NotificationTypes.MUTED)
                dismiss()
            }

            notification.setOnClickListener {
                onNotificationTypeSelectedListener.onNotificationTypeSelected(NotificationTypes.UNMUTED)
                dismiss()
            }

            adhan.setOnClickListener {
                onNotificationTypeSelectedListener.onNotificationTypeSelected(NotificationTypes.VOICE_ENABLED)
                dismiss()
            }
        }
    }
}