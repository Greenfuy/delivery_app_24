package com.kpfu.itis.android_inception_23.presentation

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.kpfu.itis.android_inception_23.R
import com.itis.delivery.presentation.base.BaseActivity
import com.itis.delivery.presentation.base.BaseFragment
import com.kpfu.itis.android_inception_23.presentation.screens.weatherinfo.WeatherInfoFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity(override val fragmentContainerId: Int) : BaseActivity() {


}