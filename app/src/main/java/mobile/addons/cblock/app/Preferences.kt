package mobile.addons.cblock.app

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import mobile.addons.cblock.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper for [SharedPreferences]
 */

@Singleton
class Preferences @Inject internal constructor(context: Context) {

    private val serviceSwitchKey = "serviceSwitchKey"
    private val pref: SharedPreferences = with(context) { getSharedPreferences(getString(R.string.app_preferences_file), MODE_PRIVATE) }

    var serviceState: Boolean
        get() = pref.getBoolean(serviceSwitchKey, false)
        set(value) {
            val editor = pref.edit()
            editor.putBoolean(serviceSwitchKey, value)
            editor.apply()
        }

}
