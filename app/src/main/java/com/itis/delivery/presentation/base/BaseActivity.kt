package com.itis.delivery.presentation.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(@LayoutRes layoutRes: Int) : AppCompatActivity(layoutRes) {

    protected abstract val fragmentContainerId: Int

}