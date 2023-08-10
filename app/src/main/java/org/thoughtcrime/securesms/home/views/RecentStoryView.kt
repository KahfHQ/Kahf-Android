package org.thoughtcrime.securesms.home.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.RecentStoryViewBinding

class RecentStoryView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var binding: RecentStoryViewBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.recent_story_view, this)
        binding = RecentStoryViewBinding.bind(this)
        setData()
    }

    private fun setData() {
        binding.apply {

        }
    }
}