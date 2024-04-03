package com.itis.delivery.presentation

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.databinding.ActivityMainBinding
import com.itis.delivery.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    override val fragmentContainerId: Int = R.id.main_activity_container
    private val viewBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}