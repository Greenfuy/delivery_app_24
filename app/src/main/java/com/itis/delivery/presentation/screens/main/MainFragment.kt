package com.itis.delivery.presentation.screens.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.databinding.FragmentMainBinding
import com.itis.delivery.presentation.base.BaseFragment

class MainFragment : BaseFragment(R.layout.fragment_main) {

    //private val viewModel: MainViewModel by viewModels()
    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}