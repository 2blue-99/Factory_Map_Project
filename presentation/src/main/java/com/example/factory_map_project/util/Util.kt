package com.example.factory_map_project.util

import android.app.Activity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object Util {

    const val LONG_ANI = 500

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    /**
     * 상태바 투명 처리
     */
    fun Activity.setStatusBarTransparent() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}