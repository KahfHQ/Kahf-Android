package org.thoughtcrime.securesms.home.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import com.bumptech.glide.Glide
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.RecentStoryViewBinding
import org.thoughtcrime.securesms.recipients.Recipient

class RecentStoryView constructor(context: Context,
        private val storyModel: StoryModel) : ConstraintLayout(context) {

    private var binding: RecentStoryViewBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.recent_story_view, this)
        binding = RecentStoryViewBinding.bind(this)
        setData()
    }

    private fun setData() {
        binding.apply {
            Glide.with(this@RecentStoryView)
                    .load(storyModel.storyUrl)
                    .into(image)

            avatar.setRecipient(storyModel.storyOwner)
            name.text = storyModel.storyOwner.profileName.toString()
            date.text = storyModel.storyDate
        }
    }
}

data class StoryModel(
        val storyOwner: Recipient,
        val storyUrl: String,
        val storyDate: String
)