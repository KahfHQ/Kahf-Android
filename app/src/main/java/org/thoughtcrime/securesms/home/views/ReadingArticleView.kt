package org.thoughtcrime.securesms.home.views

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.ReadingArticleViewBinding

class ReadingArticleView constructor(context: Context, private val model: ReadingArticleModel) : ConstraintLayout(context) {
    private val binding: ReadingArticleViewBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.reading_article_view, this)
        binding = ReadingArticleViewBinding.bind(this)
        setData()
    }

    private fun setData() {
        binding.apply {
            Glide.with(this@ReadingArticleView)
                    .load(model.imageUri)
                    .into(image)
            title.text = model.title
            description.text = model.description
            continueBtn.setOnClickListener {

            }
        }
    }
}

data class ReadingArticleModel(
        val imageUri: String,
        val title: String,
        val description: String,
)