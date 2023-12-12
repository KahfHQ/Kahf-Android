package org.thoughtcrime.securesms.prayers.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.PrayerModelViewBinding
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants
import org.thoughtcrime.securesms.prayers.fragments.AdhanAndNotificationBottomSheetFragment
import org.thoughtcrime.securesms.prayers.landing.PrayersLandingFragment
import org.thoughtcrime.securesms.prayers.landing.SharedPrefKey
import org.thoughtcrime.securesms.prayers.listeners.OnNotificationTypeSelectedListener
import org.thoughtcrime.securesms.prayers.utils.NotificationUtils
import java.util.Calendar
import java.util.Date

class PrayerModelView constructor(context: Context, val prayerModel: PrayerModel, private val fragment: PrayersLandingFragment) : ConstraintLayout(context) {

    private var binding: PrayerModelViewBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.prayer_model_view, this)
        binding = PrayerModelViewBinding.bind(this)
        setData()
    }

    private fun setData() {
        binding.apply {
            timeNameLabel.text = prayerModel.label
            timeLabel.text = prayerModel.time
            notificationTypeImage.setImageDrawable(
                when(prayerModel.notificationType) {
                    PrayersConstants.NotificationTypes.MUTED -> ContextCompat.getDrawable(context, R.drawable.ic_prayer_muted)
                    PrayersConstants.NotificationTypes.UNMUTED -> ContextCompat.getDrawable(context, R.drawable.ic_prayer_unmuted)
                    else -> ContextCompat.getDrawable(context, R.drawable.ic_prayer_voice_enabled)
                }
            )

            val now = Date()
            if (fragment.dateIndex != 0) {
                if (now > prayerModel.date) {
                    changeToDisabledModel()
                } else {
                    notificationTypeImage.setOnClickListener {
                        val bottomSheetFragment = AdhanAndNotificationBottomSheetFragment(prayerModel.notificationType, prepareOnNotificationSelectedListener())
                        bottomSheetFragment.show(fragment.parentFragmentManager, bottomSheetFragment.tag)
                    }
                }
            } else {
                changeToDisabledModel()
            }
        }
    }

    private fun changeToDisabledModel() {
        binding.apply {
            ImageViewCompat.setImageTintList(notificationTypeImage, ColorStateList.valueOf(Color.parseColor("#CCCCCC")))
            containerView.background = ContextCompat.getDrawable(context, R.drawable.disabled_prayer_rounded_background)
            timeNameLabel.setTextColor(Color.parseColor("#CCCCCC"))
            timeLabel.setTextColor(Color.parseColor("#CCCCCC"))
        }
    }

    private fun prepareOnNotificationSelectedListener(): OnNotificationTypeSelectedListener {
        return object : OnNotificationTypeSelectedListener {
            override fun onNotificationTypeSelected(notificationType: String) {
                if (notificationType != prayerModel.notificationType) {
                    val notificationId = fragment.timeToNotificationId(prayerModel.date.time)

                    when(notificationType) {
                        PrayersConstants.NotificationTypes.MUTED -> {
                            binding.notificationTypeImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_prayer_muted))
                            NotificationUtils.cancelNotification(context, notificationId)
                            fragment.removeFromSharedPref(notificationId)
                        }
                        PrayersConstants.NotificationTypes.UNMUTED -> {
                            binding.notificationTypeImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_prayer_unmuted))
                            NotificationUtils.scheduleNotification(context, "It is prayer time for ${prayerModel.label}", prayerModel.date.time, notificationId)
                            fragment.saveToSharedPref(notificationId, notificationType)
                        }
                        PrayersConstants.NotificationTypes.VOICE_ENABLED -> {
                            binding.notificationTypeImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_prayer_voice_enabled))
                            NotificationUtils.scheduleNotification(context, "It is prayer time for ${prayerModel.label}", prayerModel.date.time, notificationId)
                            fragment.saveToSharedPref(notificationId, notificationType)
                        }
                        else -> {}
                    }

                    prayerModel.notificationType = notificationType
                }
            }
        }
    }
}

data class PrayerModel(
    val type: PrayersConstants.PrayerTime,
    val label: String?,
    val time: String?,
    var notificationType: String,
    var date: Date
)