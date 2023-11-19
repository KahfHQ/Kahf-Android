package org.thoughtcrime.securesms.home.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mobilecoin.api.MobileCoinAPI.MintConfigTx
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.UpcomingEventViewBinding

class UpcomingEventView constructor(context: Context, private val model: UpcomingEventModel?, private val colorCode: String) : ConstraintLayout(context) {
    private var binding: UpcomingEventViewBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.upcoming_event_view, this)
        binding = UpcomingEventViewBinding.bind(this)
        setData()
    }

    private fun setData() {
        binding.apply {
            containerView.background = DynamicSolidColorDrawable(colorCode)
            day.text = model?.day
            month.text = model?.month
            eventName.text = model?.eventName
            remainingTime.text = model?.remainingTime
        }
    }
}

class DynamicSolidColorDrawable(private val colorString: String) : Drawable() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun draw(canvas: Canvas) {
        val dynamicColor = Color.parseColor(colorString)
        paint.color = dynamicColor
        val rect = RectF(bounds)
        canvas.drawRoundRect(rect, 10f, 10f, paint)
    }

    override fun setAlpha(alpha: Int) {
        // Not needed for this Drawable
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Not needed for this Drawable
    }

    override fun getOpacity(): Int {
        return android.graphics.PixelFormat.OPAQUE
    }
}

data class UpcomingEventModel(
    val day: String?,
    val month: String?,
    val eventName: String?,
    val remainingTime: String?
)