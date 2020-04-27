package com.titaniocorp.pos.app.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import androidx.preference.PreferenceFragmentCompat
import com.titaniocorp.pos.R
import com.titaniocorp.pos.di.Injectable
import com.titaniocorp.pos.util.Configurations
import com.titaniocorp.pos.util.Configurations.PROFIT_PERCENT_PREFERENCE
import com.titaniocorp.pos.util.Configurations.TAX_PERCENT_PREFERENCE

/**
 * Fragmento que maneja las preferencias del sistema
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class SettingsFragment: PreferenceFragmentCompat(),
    Injectable,
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key){
            getString(R.string.preference_key_language) -> {}
            else -> {
                Configurations.update(
                    key,
                    sharedPreferences.getString(key, "0") ?: "0"
                )
            }
        }
    }
}