/*
 * SPDX-FileCopyrightText: 2026 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.nothing.dirac.util

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.provider.Settings

class BlackThemeObserver(private val context: Context) :
    ContentObserver(Handler(Looper.getMainLooper())) {

    private val _enabled = MutableStateFlow(readValue())
    val enabled: StateFlow<Boolean> = _enabled.asStateFlow()

    init {
        context.contentResolver.registerContentObserver(
            Settings.Secure.getUriFor(Settings.Secure.BERRY_BLACK_THEME),
            false,
            this,
        )
    }

    override fun onChange(selfChange: Boolean) {
        _enabled.value = readValue()
    }

    private fun readValue() =
        Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.BERRY_BLACK_THEME,
            0,
        ) != 0

    fun release() {
        context.contentResolver.unregisterContentObserver(this)
    }
}
