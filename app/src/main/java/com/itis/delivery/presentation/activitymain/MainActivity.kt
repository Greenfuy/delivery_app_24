package com.itis.delivery.presentation.activitymain

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.delivery.R
import com.itis.delivery.databinding.ActivityMainBinding
import com.itis.delivery.presentation.base.BaseActivity
import com.itis.delivery.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    override val fragmentContainerId: Int = R.id.main_activity_container
    private val viewBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    private val prefs: SharedPreferences by lazy {
        getSharedPreferences(DELIVERY_PREFS, MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val controller = (supportFragmentManager
            .findFragmentById(R.id.main_activity_container) as NavHostFragment)
            .navController

        findViewById<BottomNavigationView>(R.id.bnv_main).apply {
            setupWithNavController(controller)
        }

        controller.addOnDestinationChangedListener { _, destination, _ ->
            if (
                destination.id == R.id.signInFragment
                || destination.id == R.id.signUpFragment
                || destination.id == R.id.productPageFragment
                || destination.id == R.id.resultSearchFragment
                || destination.id == R.id.orderFragment
                ) {

                viewBinding.bnvMain.visibility = View.GONE
            } else {
                viewBinding.bnvMain.visibility = View.VISIBLE
            }
        }

        when (prefs.getInt(THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (prefs.getBoolean(FIRST_RUN, true)) {
            findNavController(R.id.main_activity_container).safeNavigate(
                R.id.mainFragment,
                R.id.action_mainFragment_to_signInFragment
            )
            prefs.edit().putBoolean(FIRST_RUN, false).apply()
        }
    }

    companion object {
        const val DELIVERY_PREFS = "ru.itis.delivery"
        private const val FIRST_RUN = "first_run"
        const val THEME_MODE = "theme_mode"
    }
}