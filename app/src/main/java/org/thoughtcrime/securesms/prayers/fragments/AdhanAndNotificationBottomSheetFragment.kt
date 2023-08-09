package org.thoughtcrime.securesms.prayers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.thoughtcrime.securesms.R

class AdhanAndNotificationBottomSheetFragment: BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.adhan_and_navigation_bottom_sheet_fragment, container, false)
    }
}