package com.itis.delivery.presentation.screens.settings

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itis.delivery.R

class AboutBottomSheetFragment : BottomSheetDialogFragment(R.layout.fragment_about_bottom_sheet) {

    companion object {
        const val ABOUT_BOTTOM_SHEET_FRAGMENT_TAG = "ABOUT_BOTTOM_SHEET_FRAGMENT_TAG"
        fun newInstance() = AboutBottomSheetFragment()
    }
}