package org.thoughtcrime.securesms.home.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.UpcomingEventsViewBinding

class UpcomingEventsView constructor(context: Context, private val upcomingEvents: List<UpcomingEventModel>?) : ConstraintLayout(context) {
    private var binding: UpcomingEventsViewBinding
    private val colorCodes = listOf("#333E8DFF", "#339A77FF", "#33333333")
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.upcoming_events_view, this)
        binding = UpcomingEventsViewBinding.bind(this)
        setData()
    }

    private fun setData() {
        binding.apply {
            upcomingEvents?.forEachIndexed { index, it ->
                eventsList.addView(UpcomingEventView(context, UpcomingEventModel(it.day, it.month, it.eventName, it.remainingTime), colorCodes[index % 3]))
            }

            allEventsBtn.setOnClickListener {
                Toast.makeText(context, "Not implemented yet!", Toast.LENGTH_LONG).show()
            }
        }
    }
}