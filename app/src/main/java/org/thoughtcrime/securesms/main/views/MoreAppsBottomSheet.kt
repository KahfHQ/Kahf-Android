package org.thoughtcrime.securesms.main.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.MoreAppsBottomSheetViewBinding
import org.thoughtcrime.securesms.main.MainActivityListHostFragment
import org.thoughtcrime.securesms.stories.tabs.ConversationListTabsViewModel
import org.thoughtcrime.securesms.util.navigation.safeNavigate

class MoreAppsBottomSheet constructor(private val fragment: MainActivityListHostFragment,
                                      private val navController: NavController): BottomSheetDialogFragment() {

    private lateinit var binding: MoreAppsBottomSheetViewBinding
    private val viewModel: ConversationListTabsViewModel by viewModels(ownerProducer = { fragment.requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.more_apps_bottom_sheet_view, container, false)
        binding = MoreAppsBottomSheetViewBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            stories.setOnClickListener {
                navController.safeNavigate(R.id.action_global_storiesLandingFragment)
                dismiss()
            }

            mosqueNearby.setOnClickListener {
                viewModel.onHomeSelected()
                dismiss()
            }
        }

    }
}