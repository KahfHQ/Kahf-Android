package org.thoughtcrime.securesms.stories.landing

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.thoughtcrime.securesms.MainActivity
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.components.AvatarImageView
import org.thoughtcrime.securesms.components.settings.DSLConfiguration
import org.thoughtcrime.securesms.components.settings.DSLSettingsFragment
import org.thoughtcrime.securesms.components.settings.DSLSettingsText
import org.thoughtcrime.securesms.components.settings.configure
import org.thoughtcrime.securesms.conversation.ConversationIntents
import org.thoughtcrime.securesms.conversation.mutiselect.forward.MultiselectForwardFragment
import org.thoughtcrime.securesms.conversation.mutiselect.forward.MultiselectForwardFragmentArgs
import org.thoughtcrime.securesms.conversationlist.ConversationListFragment.Callback
import org.thoughtcrime.securesms.database.model.MediaMmsMessageRecord
import org.thoughtcrime.securesms.database.model.MmsMessageRecord
import org.thoughtcrime.securesms.database.model.StoryViewState
import org.thoughtcrime.securesms.main.Material3OnScrollHelperBinder
import org.thoughtcrime.securesms.mediasend.v2.MediaSelectionActivity
import org.thoughtcrime.securesms.mediasend.v2.MediaSelectionActivity.Companion.camera
import org.thoughtcrime.securesms.permissions.Permissions
import org.thoughtcrime.securesms.safety.SafetyNumberBottomSheet
import org.thoughtcrime.securesms.stories.StoryTextPostModel
import org.thoughtcrime.securesms.stories.StoryViewerArgs
import org.thoughtcrime.securesms.stories.dialogs.StoryContextMenu
import org.thoughtcrime.securesms.stories.dialogs.StoryDialogs
import org.thoughtcrime.securesms.stories.my.MyStoriesActivity
import org.thoughtcrime.securesms.stories.tabs.ConversationListTab
import org.thoughtcrime.securesms.stories.tabs.ConversationListTabsViewModel
import org.thoughtcrime.securesms.stories.viewer.StoryViewerActivity
import org.thoughtcrime.securesms.util.LifecycleDisposable
import org.thoughtcrime.securesms.util.adapter.mapping.MappingAdapter
import org.thoughtcrime.securesms.util.fragments.requireListener
import org.thoughtcrime.securesms.util.runHideAnimation
import org.thoughtcrime.securesms.util.visible
import java.util.concurrent.TimeUnit

/**
 * The "landing page" for Stories.
 */
class StoriesLandingFragment : DSLSettingsFragment(layoutId = R.layout.stories_landing_fragment) {

  companion object {
    private const val LIST_SMOOTH_SCROLL_TO_TOP_THRESHOLD = 25
  }

  private lateinit var emptyNotice: View
  private lateinit var cameraFab: FloatingActionButton
  private lateinit var addStoryBtn: TextView
  private var toolbarAvatar: AvatarImageView? = null
  private var toolbar: Toolbar? = null

  private val lifecycleDisposable = LifecycleDisposable()

  private val viewModel: StoriesLandingViewModel by viewModels(
    factoryProducer = {
      StoriesLandingViewModel.Factory(StoriesLandingRepository(requireContext()))
    }
  )

  private val tabsViewModel: ConversationListTabsViewModel by viewModels(ownerProducer = { requireActivity() })

  private lateinit var adapter: MappingAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    setHasOptionsMenu(true)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    toolbar = (parentFragment?.parentFragment as? Callback)?.storiesToolbar
    toolbar?.findViewById<View>(R.id.add_story)?.setOnClickListener {
      cameraFab.performClick()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    (requireActivity() as? MainActivity)?.showBottomTabBar()
    toolbar?.visible = false
  }

//  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//    menu.clear()
//    inflater.inflate(R.menu.story_landing_menu, menu)
//  }

  override fun onResume() {
    super.onResume()
    viewModel.isTransitioningToAnotherScreen = false
    (requireActivity() as? MainActivity)?.hideBottomTabBar()
  }

  override fun bindAdapter(adapter: MappingAdapter) {
    this.adapter = adapter

    StoriesLandingItem.register(adapter)
//    MyStoriesItem.register(adapter)
    ExpandHeader.register(adapter)

    recyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
    recyclerView?.addItemDecoration(ItemOffsetDecoration(requireContext()))
    requireListener<Material3OnScrollHelperBinder>().bindScrollHelper(recyclerView!!)

    lifecycleDisposable.bindTo(viewLifecycleOwner)
    emptyNotice = requireView().findViewById(R.id.empty_notice)
    cameraFab = requireView().findViewById(R.id.camera_fab)
//    addStoryBtn = requireView().findViewById(R.id.add_story)

    sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.change_transform_fabs)
    setEnterSharedElementCallback(object : SharedElementCallback() {
      override fun onSharedElementStart(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?, sharedElementSnapshots: MutableList<View>?) {
        if (sharedElementNames?.contains("camera_fab") == true) {
          cameraFab.setImageResource(R.drawable.ic_compose_outline_24)
          lifecycleDisposable += Single.timer(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
              cameraFab.setImageResource(R.drawable.ic_add_story_fab)
            }
        }
      }
    })

    val backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.kahf_selected_icon_blue))
    val imageTintList = ColorStateList.valueOf(Color.WHITE)
    cameraFab.backgroundTintList = backgroundTintList
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      cameraFab.imageTintList = imageTintList
    } else {
      ImageViewCompat.setImageTintList(cameraFab, imageTintList)
    }
    cameraFab.setOnClickListener {
      Permissions.with(this)
        .request(Manifest.permission.CAMERA)
        .ifNecessary()
        .withRationaleDialog(getString(R.string.ConversationActivity_to_capture_photos_and_video_allow_signal_access_to_the_camera), R.drawable.ic_camera_24)
        .withPermanentDenialDialog(getString(R.string.ConversationActivity_signal_needs_the_camera_permission_to_take_photos_or_video))
        .onAllGranted {
          startActivityIfAble(MediaSelectionActivity.camera(requireContext(), isStory = true))
        }
        .onAnyDenied { Toast.makeText(requireContext(), R.string.ConversationActivity_signal_needs_camera_permissions_to_take_photos_or_video, Toast.LENGTH_LONG).show() }
        .execute()
    }

//    addStoryBtn.setOnClickListener {
//      cameraFab.performClick()
//    }

    viewModel.state.observe(viewLifecycleOwner) {
      if (it.loadingState == StoriesLandingState.LoadingState.LOADED) {
        adapter.submitList(getConfiguration(it).toMappingModelList())
        emptyNotice.visible = it.hasNoStories
      }
    }

    requireActivity().onBackPressedDispatcher.addCallback(
      viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          tabsViewModel.onChatsSelected()
        }
      }
    )

//    lifecycleDisposable += tabsViewModel.tabClickEvents
//      .filter { it == ConversationListTab.STORIES }
//      .subscribeBy(onNext = {
//        val layoutManager = recyclerView?.layoutManager as? LinearLayoutManager ?: return@subscribeBy
//        if (layoutManager.findFirstVisibleItemPosition() <= LIST_SMOOTH_SCROLL_TO_TOP_THRESHOLD) {
//          recyclerView?.smoothScrollToPosition(0)
//        } else {
//          recyclerView?.scrollToPosition(0)
//        }
//      })
  }

  private fun getConfiguration(state: StoriesLandingState): DSLConfiguration {
    return configure {
      val (stories, hidden) = state.storiesLandingItems.map {
        createStoryLandingItem(it)
      }.partition {
        !it.data.isHidden
      }

//      if (state.displayMyStoryItem) {
//        customPref(
//          MyStoriesItem.Model(
//            onClick = {
//              cameraFab.performClick()
//            }
//          )
//        )
//      }

      stories.forEach { item ->
        customPref(item)
      }

      if (hidden.isNotEmpty()) {
        customPref(
          ExpandHeader.Model(
            title = DSLSettingsText.from(R.string.StoriesLandingFragment__hidden_stories),
            isExpanded = state.isHiddenContentVisible,
            onClick = { viewModel.setHiddenContentVisible(it) }
          )
        )
      }

      if (state.isHiddenContentVisible) {
        hidden.forEach { item ->
          customPref(item)
        }
      }
    }
  }

  private fun createStoryLandingItem(data: StoriesLandingItemData): StoriesLandingItem.Model {
    return StoriesLandingItem.Model(
      data = data,
      onRowClick = { model, preview ->
        openStoryViewer(model, preview, false)
      },
      onForwardStory = {
        MultiselectForwardFragmentArgs.create(requireContext(), it.data.primaryStory.multiselectCollection.toSet()) { args ->
          MultiselectForwardFragment.showBottomSheet(childFragmentManager, args)
        }
      },
      onGoToChat = {
        startActivityIfAble(ConversationIntents.createBuilder(requireContext(), it.data.storyRecipient.id, -1L).build())
      },
      onHideStory = {
        if (!it.data.isHidden) {
          handleHideStory(it)
        } else {
          lifecycleDisposable += viewModel.setHideStory(it.data.storyRecipient, !it.data.isHidden).subscribe()
        }
      },
      onShareStory = {
        StoryContextMenu.share(this@StoriesLandingFragment, it.data.primaryStory.messageRecord as MediaMmsMessageRecord)
      },
      onSave = {
        StoryContextMenu.save(requireContext(), it.data.primaryStory.messageRecord)
      },
      onDeleteStory = {
        handleDeleteStory(it)
      },
      onInfo = { model, preview ->
        openStoryViewer(model, preview, true)
      },
      onAvatarClick = {
        cameraFab.performClick()
      }
    )
  }

  private fun openStoryViewer(model: StoriesLandingItem.Model, preview: View, isFromInfoContextMenuAction: Boolean) {
    if (model.data.storyRecipient.isMyStory) {
      startActivityIfAble(Intent(requireContext(), MyStoriesActivity::class.java))
    } else if (model.data.primaryStory.messageRecord.isOutgoing && model.data.primaryStory.messageRecord.isFailed) {
      if (model.data.primaryStory.messageRecord.isIdentityMismatchFailure) {
        SafetyNumberBottomSheet
          .forMessageRecord(requireContext(), model.data.primaryStory.messageRecord)
          .show(childFragmentManager)
      } else {
        StoryDialogs.resendStory(requireContext()) {
          lifecycleDisposable += viewModel.resend(model.data.primaryStory.messageRecord).subscribe()
        }
      }
    } else {
      val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), preview, ViewCompat.getTransitionName(preview) ?: "")

      val record = model.data.primaryStory.messageRecord as MmsMessageRecord
      val blur = record.slideDeck.thumbnailSlide?.placeholderBlur
      val (text: StoryTextPostModel?, image: Uri?) = if (record.storyType.isTextStory) {
        StoryTextPostModel.parseFrom(record) to null
      } else {
        null to record.slideDeck.thumbnailSlide?.uri
      }

      startActivityIfAble(
        StoryViewerActivity.createIntent(
          context = requireContext(),
          storyViewerArgs = StoryViewerArgs(
            recipientId = model.data.storyRecipient.id,
            storyId = -1L,
            isInHiddenStoryMode = model.data.isHidden,
            storyThumbTextModel = text,
            storyThumbUri = image,
            storyThumbBlur = blur,
            recipientIds = viewModel.getRecipientIds(model.data.isHidden, model.data.storyViewState == StoryViewState.UNVIEWED),
            isFromInfoContextMenuAction = isFromInfoContextMenuAction
          )
        ),
        options.toBundle()
      )
    }
  }

  private fun handleDeleteStory(model: StoriesLandingItem.Model) {
    lifecycleDisposable += StoryContextMenu.delete(requireContext(), setOf(model.data.primaryStory.messageRecord)).subscribe()
  }

  @SuppressLint("CheckResult")
  private fun handleHideStory(model: StoriesLandingItem.Model) {
    MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_Signal_MaterialAlertDialog)
      .setTitle(R.string.StoriesLandingFragment__hide_story)
      .setMessage(getString(R.string.StoriesLandingFragment__new_story_updates, model.data.storyRecipient.getShortDisplayName(requireContext())))
      .setPositiveButton(R.string.StoriesLandingFragment__hide) { _, _ ->
        viewModel.setHideStory(model.data.storyRecipient, true).subscribe {
          Snackbar.make(cameraFab, R.string.StoriesLandingFragment__story_hidden, Snackbar.LENGTH_SHORT)
            .setAnchorView(cameraFab)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
            .show()
        }
      }
      .setNegativeButton(android.R.string.cancel) { _, _ -> }
      .show()
  }

//  override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    return if (item.itemId == R.id.action_settings) {
//      startActivityIfAble(StorySettingsActivity.getIntent(requireContext()))
//      true
//    } else {
//      false
//    }
//  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    Permissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
  }

  private fun startActivityIfAble(intent: Intent, options: Bundle? = null) {
    if (viewModel.isTransitioningToAnotherScreen) {
      return
    }

    viewModel.isTransitioningToAnotherScreen = true
    startActivity(intent, options)
  }

  private fun realValuedDPToPixelConversion(context: Context, dp: Float) : Float {
    val displayMetrics = context.resources.displayMetrics
    return dp * (displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
  }


  inner class ItemOffsetDecoration(var context: Context) : RecyclerView.ItemDecoration() {
    private val spacing = realValuedDPToPixelConversion(context, 10f).toInt()
    private val spanCount = 2

    override fun getItemOffsets(
      outRect: Rect,
      view: View,
      parent: RecyclerView,
      state: RecyclerView.State
    ) {
      val position = parent.getChildAdapterPosition(view)
      val column = position % spanCount

      outRect.left = spacing - column * spacing / spanCount
      outRect.right = (column + 1) * spacing / spanCount

      if (position < spanCount) {
        outRect.top = spacing
      }
      outRect.bottom =  spacing
    }
  }
}
