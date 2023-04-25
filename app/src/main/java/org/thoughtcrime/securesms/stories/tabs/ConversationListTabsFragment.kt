package org.thoughtcrime.securesms.stories.tabs

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import org.signal.core.util.DimensionUnit
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.util.visible
import java.text.NumberFormat

/**
 * Displays the "Chats" and "Stories" tab to a user.
 */
class ConversationListTabsFragment : Fragment(R.layout.conversation_list_tabs) {

  private val viewModel: ConversationListTabsViewModel by viewModels(ownerProducer = { requireActivity() })

  private lateinit var chatsUnreadIndicator: TextView
  private lateinit var storiesUnreadIndicator: TextView
  private lateinit var chatsIcon: LottieAnimationView
  private lateinit var settingsIcon: LottieAnimationView
  private lateinit var storiesIcon: LottieAnimationView
  private lateinit var chatsPill: ImageView
  private lateinit var storiesPill: ImageView
  private lateinit var settingsPill: ImageView

  private var pillAnimator: Animator? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    chatsUnreadIndicator = view.findViewById(R.id.chats_unread_indicator)
    storiesUnreadIndicator = view.findViewById(R.id.stories_unread_indicator)
    chatsIcon = view.findViewById(R.id.chats_tab_icon)
    storiesIcon = view.findViewById(R.id.stories_tab_icon)
    settingsIcon = view.findViewById(R.id.settings_tab_icon)
    chatsPill = view.findViewById(R.id.chats_pill)
    storiesPill = view.findViewById(R.id.stories_pill)
    settingsPill = view.findViewById(R.id.settings_pill)

    val iconTint = ContextCompat.getColor(requireContext(), R.color.signal_colorOnSecondaryContainer)

    chatsIcon.addValueCallback(
      KeyPath("**"),
      LottieProperty.COLOR
    ) { iconTint }

    storiesIcon.addValueCallback(
      KeyPath("**"),
      LottieProperty.COLOR
    ) { iconTint }

    settingsIcon.addValueCallback(
      KeyPath("**"),
      LottieProperty.COLOR
    ) { iconTint }

    view.findViewById<View>(R.id.chats_tab_touch_point).setOnClickListener {
      viewModel.onChatsSelected()
    }

    view.findViewById<View>(R.id.settings_tab_touch_point).setOnClickListener {
      viewModel.onSettingsSelected()
    }

    view.findViewById<View>(R.id.stories_tab_touch_point).setOnClickListener {
      viewModel.onStoriesSelected()
    }

    update(viewModel.stateSnapshot, true)

    viewModel.state.observe(viewLifecycleOwner) {
      update(it, false)
      println("here")
    }
  }

  private fun update(state: ConversationListTabsState, immediate: Boolean) {
    chatsIcon.isSelected = state.tab == ConversationListTab.CHATS
    storiesIcon.isSelected = state.tab == ConversationListTab.STORIES
    settingsIcon.isSelected = state.tab == ConversationListTab.SETTINGS

    chatsPill.isSelected = chatsIcon.isSelected
    storiesPill.isSelected = storiesIcon.isSelected
    settingsPill.isSelected = settingsIcon.isSelected

    val hasStateChange = chatsIcon.isSelected or settingsIcon.isSelected or storiesIcon.isSelected
    if (immediate) {
      chatsIcon.pauseAnimation()
      storiesIcon.pauseAnimation()
      /*settingsIcon.pauseAnimation()*/

      chatsIcon.progress = if (chatsIcon.isSelected) 1f else 0f
      storiesIcon.progress = if (storiesIcon.isSelected) 1f else 0f
      settingsIcon.progress = if (settingsIcon.isSelected) 1f else 0f

      runPillAnimation(0, chatsPill, storiesPill, settingsPill)
    } else if (hasStateChange) {
      runLottieAnimations(chatsIcon, storiesIcon, /*settingsIcon*/)
      runPillAnimation(150, chatsPill, storiesPill, /*settingsIcon*/)
    }

    chatsUnreadIndicator.visible = state.unreadChatsCount > 0
    chatsUnreadIndicator.text = formatCount(state.unreadChatsCount)

    storiesUnreadIndicator.visible = state.unreadStoriesCount > 0
    storiesUnreadIndicator.text = formatCount(state.unreadStoriesCount)

    requireView().visible = state.visibilityState.isVisible()
  }

  private fun runLottieAnimations(vararg toAnimate: LottieAnimationView) {
    toAnimate.forEach {
      if (it.isSelected) {
        it.resumeAnimation()
      } else {
        if (it.isAnimating) {
          it.pauseAnimation()
        }

        it.progress = 0f
      }
    }
  }

  private fun runPillAnimation(duration: Long, vararg toAnimate: ImageView) {
    val (selected, unselected) = toAnimate.partition { it.isSelected }

    pillAnimator?.cancel()
    pillAnimator = AnimatorSet().apply {
      this.duration = duration
      interpolator = PathInterpolatorCompat.create(0.17f, 0.17f, 0f, 1f)
      playTogether(
        selected.map { view ->
          view.visibility = View.VISIBLE
          ValueAnimator.ofInt(view.paddingLeft, 0).apply {
            addUpdateListener {
              view.setPadding(it.animatedValue as Int, 0, it.animatedValue as Int, 0)
            }
          }
        }
      )
      start()
    }

    unselected.forEach {
      val smallPad = DimensionUnit.DP.toPixels(16f).toInt()
      it.setPadding(smallPad, 0, smallPad, 0)
      it.visibility = View.INVISIBLE
    }
  }

  private fun formatCount(count: Long): String {
    if (count > 99L) {
      return getString(R.string.ConversationListTabs__99p)
    }
    return NumberFormat.getInstance().format(count)
  }
}
