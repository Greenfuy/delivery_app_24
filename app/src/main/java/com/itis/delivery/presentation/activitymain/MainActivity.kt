package com.itis.delivery.presentation.activitymain

import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
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
        getSharedPreferences("ru.itis.delivery", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
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
        private const val FIRST_RUN = "first_run"
    }
}