package com.koniukhov.cinecircle.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        intent.getIntExtra(
            "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
            0
        ).takeIf { it != 0 }?.let { themeId ->
            setTheme(themeId)
        }

        super.onCreate(savedInstanceState)
    }
}