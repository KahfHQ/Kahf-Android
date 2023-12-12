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
//  private lateinit var storiesUnreadIndicator: TextView
  private lateinit var homeIcon: LottieAnimationView
  private lateinit var chatsIcon: LottieAnimationView
  private lateinit var settingsIcon: LottieAnimationView
  private lateinit var prayersIcon: LottieAnimationView
  private lateinit var chatAppsIcon: LottieAnimationView
  private lateinit var homePill: ImageView
  private lateinit var chatsPill: ImageView
  private lateinit var chatAppsPill: ImageView
  private lateinit var prayersPill: ImageView
  private lateinit var settingsPill: ImageView
  private lateinit var homeLabel: TextView
  private lateinit var chatsLabel: TextView
  private lateinit var chatAppsLabel: TextView
  private lateinit var prayersLabel: TextView
  private lateinit var settingsLabel: TextView

  private var pillAnimator: Animator? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    chatsUnreadIndicator = view.findViewById(R.id.chats_unread_indicator)
//    storiesUnreadIndicator = view.findViewById(R.id.stories_unread_indicator)
    homeIcon = view.findViewById(R.id.home_tab_icon)
    chatsIcon = view.findViewById(R.id.chats_tab_icon)
    chatAppsIcon = view.findViewById(R.id.chat_apps_tab_icon)
    prayersIcon = view.findViewById(R.id.prayers_tab_icon)
    settingsIcon = view.findViewById(R.id.settings_tab_icon)
    homePill = view.findViewById(R.id.home_pill)
    chatsPill = view.findViewById(R.id.chats_pill)
    chatAppsPill = view.findViewById(R.id.chat_apps_pill)
    prayersPill = view.findViewById(R.id.prayers_pill)
    settingsPill = view.findViewById(R.id.settings_pill)
    homeLabel = view.findViewById(R.id.home_tab_label)
    chatsLabel = view.findViewById(R.id.chats_tab_label)
    chatAppsLabel = view.findViewById(R.id.chat_apps_tab_label)
    prayersLabel = view.findViewById(R.id.prayers_tab_label)
    settingsLabel = view.findViewById(R.id.settings_tab_label)

    val iconTint = ContextCompat.getColor(requireContext(), R.color.signal_colorOnSecondaryContainer)

    homeIcon.addValueCallback(
      KeyPath("**"),
      LottieProperty.COLOR
    ) { iconTint }

    chatsIcon.addValueCallback(
      KeyPath("**"),
      LottieProperty.COLOR
    ) { iconTint }

    chatAppsIcon.addValueCallback(
      KeyPath("**"),
      LottieProperty.COLOR
    ) { iconTint }

    prayersIcon.addValueCallback(
      KeyPath("**"),
      LottieProperty.COLOR
    ) { iconTint }

    settingsIcon.addValueCallback(
      KeyPath("**"),
      LottieProperty.COLOR
    ) { iconTint }

    view.findViewById<View>(R.id.home_tab_touch_point).setOnClickListener {
      viewModel.onHomeSelected()
    }

    view.findViewById<View>(R.id.chats_tab_touch_point).setOnClickListener {
      viewModel.onChatsSelected()
    }

    view.findViewById<View>(R.id.settings_tab_touch_point).setOnClickListener {
      viewModel.onSettingsSelected()
    }

    view.findViewById<View>(R.id.prayers_tab_touch_point).setOnClickListener {
      viewModel.onPrayersSelected()
    }

    view.findViewById<View>(R.id.stories_tab_touch_point).setOnClickListener {
      viewModel.onChatAppsSelected()
    }

    update(viewModel.stateSnapshot, true)

    viewModel.state.observe(viewLifecycleOwner) {
      update(it, false)
      println("here")
    }
  }

  private fun update(state: ConversationListTabsState, immediate: Boolean) {
    homeIcon.isSelected = state.tab == ConversationListTab.HOME
    chatsIcon.isSelected = state.tab == ConversationListTab.CHATS
    chatAppsIcon.isSelected = state.tab == ConversationListTab.CHATAPPS
    prayersIcon.isSelected = state.tab == ConversationListTab.PRAYERS
//    settingsIcon.isSelected = state.tab == ConversationListTab.SETTINGS

    homePill.isSelected = homeIcon.isSelected
    chatsPill.isSelected = chatsIcon.isSelected
    chatAppsPill.isSelected = chatAppsIcon.isSelected
    prayersPill.isSelected = prayersIcon.isSelected

    homeLabel.isSelected = homeIcon.isSelected
    chatsLabel.isSelected = chatsIcon.isSelected
    chatAppsLabel.isSelected = chatAppsIcon.isSelected
    prayersLabel.isSelected = prayersIcon.isSelected

    val hasStateChange = homeIcon.isSelected or chatsIcon.isSelected or
            chatAppsIcon.isSelected or prayersIcon.isSelected
    if (immediate) {
      chatsIcon.pauseAnimation()
      chatAppsIcon.pauseAnimation()

      homeIcon.progress = if (homeIcon.isSelected) 1f else 0f
      chatsIcon.progress = if (chatsIcon.isSelected) 1f else 0f
      chatAppsIcon.progress = if (chatAppsIcon.isSelected) 1f else 0f
      prayersIcon.progress = if (prayersIcon.isSelected) 1f else 0f

      changeIcon()
      showLabel(homeLabel, chatsLabel, chatAppsLabel, prayersLabel, settingsLabel)

    } else if (hasStateChange) {
      changeIcon()
      showLabel(homeLabel, chatsLabel, chatAppsLabel, prayersLabel, settingsLabel)
//      runLottieAnimations(/*homeIcon,*/ chatsIcon, chatAppsIcon, /*prayersIcon,*/ /*settingsIcon*/)
//      runPillAnimation(150, /*homePill,*/ chatsPill, chatAppsPill, /*prayersPill,*/ /*settingsIcon*/)
    }

    chatsUnreadIndicator.visible = state.unreadChatsCount > 0
    chatsUnreadIndicator.text = formatCount(state.unreadChatsCount)

//    storiesUnreadIndicator.visible = state.unreadStoriesCount > 0
//    storiesUnreadIndicator.text = formatCount(state.unreadStoriesCount)

    requireView().visible = state.visibilityState.isVisible()
  }

  private fun changeIcon() {
    homeIcon.setImageResource(when (homeIcon.isSelected) {
      true -> R.drawable.ic_home_selected
      else -> R.drawable.ic_home_deselected
    })
    chatAppsIcon.setImageResource(when (chatAppsIcon.isSelected) {
      true -> R.drawable.ic_chat_apps_selected
      else -> R.drawable.ic_chat_apps_deselected
    })
    chatsIcon.setImageResource(when (chatsIcon.isSelected) {
      true -> R.drawable.ic_chat_selected
      else -> R.drawable.ic_chat_deselected
    })
    prayersIcon.setImageResource(when (prayersIcon.isSelected) {
      true -> R.drawable.ic_prayers_selected
      else -> R.drawable.ic_prayers_deselected
    })
//    settingsIcon.setImageResource(when (settingsIcon.isSelected) {
//      true -> R.drawable.ic_settings_selected
//      else -> R.drawable.ic_settings_deselected
//    })
  }

  private fun showLabel(vararg labels: TextView) {
    labels.forEach {
      it.visibility = when (it.isSelected) {
        true -> View.VISIBLE
        else -> View.GONE
      }
    }
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
