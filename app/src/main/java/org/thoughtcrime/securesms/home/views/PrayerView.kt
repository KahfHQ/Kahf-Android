package org.thoughtcrime.securesms.home.views

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.PrayerViewBinding

class PrayerView constructor(context: Context, private val model: PrayerModel?) : ConstraintLayout(context) {
    private var binding: PrayerViewBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.prayer_view, this)
        binding = PrayerViewBinding.bind(this)
        setData()
    }

    private fun setData() {
        binding.apply {
            title.text = model?.title
            time.text = model?.time
            description.text = model?.description
        }
    }
}

data class PrayerModel(
    val title: String,
    val time: String,
    val description: String
)