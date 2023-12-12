package org.thoughtcrime.securesms.home.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.media.Image
import android.os.Build
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.batoulapps.adhan.Prayer
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.ViewNextPrayerTimeBinding
import org.thoughtcrime.securesms.home.listeners.OnAllPrayerTimeButtonClicked
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants
import org.thoughtcrime.securesms.prayers.landing.SharedPrefKey
import org.thoughtcrime.securesms.prayers.utils.NotificationUtils
import java.util.Calendar
import java.util.Date
import kotlin.math.ceil

class NextPrayerTimeView(context: Context,
                        private val model: NextPrayerTimeModel,
                        private val onButtonClickListener: OnAllPrayerTimeButtonClicked
): ConstraintLayout(context) {

    private val binding: ViewNextPrayerTimeBinding
    private var prayerName = ""
    private var prayerTime = ""
    private var remainingTime = ""

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_next_prayer_time, this)
        binding = ViewNextPrayerTimeBinding.bind(this)
        setData()
    }

    private fun setData() {
        prayerName = getPrayerNameText(model.prayer.name)
        prayerTime = getPrayerTimeText(model.prayerDate)
        remainingTime = getRemainingTimeText(model.prayerDate)
        prepareNotificationStatus()

        binding.apply {
            prayerTimeText.text = prayerTime
            prayerNameText.text = prayerName
            prayerRemainingTimeText.text = remainingTime
            val notificationId = timeToNotificationId(model.prayerDate.time)

            notificationStatusIcon.setOnClickListener {
                when (model.notificationStatus) {
                    PrayersConstants.NotificationTypes.MUTED -> {
                        NotificationUtils.scheduleNotification(context, "Prayer Time For $prayerName", model.prayerDate.time, notificationId)
                        model.notificationStatus = PrayersConstants.NotificationTypes.UNMUTED
                        saveToSharedPref(notificationId, PrayersConstants.NotificationTypes.UNMUTED)
                    }
                    PrayersConstants.NotificationTypes.UNMUTED, PrayersConstants.NotificationTypes.VOICE_ENABLED -> {
                        NotificationUtils.cancelNotification(context, timeToNotificationId(model.prayerDate.time))
                        model.notificationStatus = PrayersConstants.NotificationTypes.MUTED
                        removeFromSharedPref(notificationId)
                    }
                }
                prepareNotificationStatus()
            }
            allPrayerTimeBtn.setOnClickListener { onButtonClickListener.onButtonClick() }
        }
    }

    private fun timeToNotificationId(value: Long) : Int {
        return ((value.div(1000L)).toInt() % Integer.MAX_VALUE)
    }

    private fun saveToSharedPref(notificationId: Int, notificationType: String) {
        val sharedPreferences = context?.getSharedPreferences("PrayersPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val key = "${SharedPrefKey.ALARM_SET_AT.value}$notificationId"
        editor?.putString(key, notificationType)
        editor?.apply()
    }

    private fun removeFromSharedPref(notificationId: Int) {
        val editor = context.getSharedPreferences("PrayersPreferences", Context.MODE_PRIVATE).edit()
        editor.remove("${SharedPrefKey.ALARM_SET_AT.value}$notificationId")
        editor.apply()
    }

    private fun prepareNotificationStatus() {
        binding.apply {
            when (model.notificationStatus) {
                PrayersConstants.NotificationTypes.MUTED -> {
                    notificationStatusIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_prayer_muted))
                    ImageViewCompat.setImageTintList(notificationStatusIcon, ColorStateList.valueOf(
                        Color.parseColor("#3E8DFF")))
                    changeBackgroundColor(Color.parseColor("#FFFFFF"))
                    notificationStatusText.text = "Unmute"
                }
                PrayersConstants.NotificationTypes.UNMUTED, PrayersConstants.NotificationTypes.VOICE_ENABLED -> {
                    notificationStatusIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_prayer_unmuted))
                    ImageViewCompat.setImageTintList(notificationStatusIcon, ColorStateList.valueOf(
                        Color.parseColor("#FFFFFF")))
                    changeBackgroundColor(Color.parseColor("#3E8DFF"))
                    notificationStatusText.text = "Mute"
                }
            }
        }
    }

    private fun changeBackgroundColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.notificationStatusIcon.backgroundTintList = ColorStateList.valueOf(color)
        } else {
            val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.home_page_next_prayer_time_mute_icon_background)
            backgroundDrawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun getRemainingTimeText(date: Date): String {
        val now = Date()
        val remainingSecs = date.time.minus(now.time).div(60000L)
        val remainingHours = remainingSecs.div(60L)
        val remainingMinutes = ceil(remainingSecs.mod(60L).toDouble()).toInt()
        return when(remainingHours > 0) {
            true -> {
                when (remainingMinutes > 0) {
                    true -> "$remainingHours Hours $remainingMinutes Minutes Left"
                    else -> "$remainingHours Hours Left"
                }
            }
            else -> {
                when (remainingMinutes > 0) {
                    true -> "$remainingMinutes Minutes Left"
                    else -> "$remainingSecs Secs Left"
                }
            }
        }
    }

    private fun getPrayerTimeText(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return when (minute > 9) {
            true -> "$hour:$minute"
            else -> "$hour:0$minute"
        }
    }

    private fun getPrayerNameText(text: String): String {
        return text.lowercase().replaceFirstChar { it.titlecase() }
    }

    fun changeTexts(time: Date, type: Prayer) {
        val prayerName = getPrayerNameText(type.name)
        val prayerTime = getPrayerTimeText(time)
        val remainingTime = getRemainingTimeText(time)

        if (this.prayerName != prayerName) {
            this.prayerName = prayerName
            binding.prayerNameText.text = prayerName
        }

        if (this.prayerTime != prayerTime) {
            this.prayerTime = prayerTime
            binding.prayerTimeText.text = prayerTime
        }

        if (this.remainingTime != remainingTime) {
            this.remainingTime = remainingTime
            binding.prayerRemainingTimeText.text = remainingTime
        }
    }
}

data class NextPrayerTimeModel(
    var notificationStatus: String,
    val prayerDate: Date,
    val prayer: Prayer
)