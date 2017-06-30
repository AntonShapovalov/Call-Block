package ru.org.adons.cblock.app

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import ru.org.adons.cblock.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper for [SharedPreferences]
 */

@Singleton
class Preferences @Inject internal constructor(context: Context) {

    private val SERVICE_SWITCH_KEY = "SERVICE_SWITCH_KEY"
    private val pref: SharedPreferences = with(context) { getSharedPreferences(getString(R.string.app_preferences_file), MODE_PRIVATE) }

    var serviceState: Boolean
        get() = pref.getBoolean(SERVICE_SWITCH_KEY, false)
        set(value) {
            val editor = pref.edit()
            editor.putBoolean(SERVICE_SWITCH_KEY, value)
            editor.apply()
        }

}
