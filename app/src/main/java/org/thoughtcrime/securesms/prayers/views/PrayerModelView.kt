package org.thoughtcrime.securesms.prayers.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.PrayerModelViewBinding
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants

class PrayerModelView constructor(context: Context, private val prayerModel: PrayerModel) : ConstraintLayout(context) {

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
        }
    }

    fun changeToYesterdayModel() {
        binding.apply {
            ImageViewCompat.setImageTintList(notificationTypeImage, ColorStateList.valueOf(Color.parseColor("#CCCCCC")))
            containerView.background = ContextCompat.getDrawable(context, R.drawable.disabled_prayer_rounded_background)
            timeNameLabel.setTextColor(Color.parseColor("#CCCCCC"))
            timeLabel.setTextColor(Color.parseColor("#CCCCCC"))
        }
    }

    fun changeToNormalModel() {
        binding.apply {
            ImageViewCompat.setImageTintList(notificationTypeImage, null)
            containerView.background = ContextCompat.getDrawable(context, R.drawable.common_rounded_background)
            timeNameLabel.setTextColor(Color.parseColor("#333333"))
            timeLabel.setTextColor(Color.parseColor("#4F4F4F"))
        }
    }
}

data class PrayerModel(
    val label: String?,
    val time: String?,
    val notificationType: String?
)