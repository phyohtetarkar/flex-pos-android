package com.jsoft.pos.ui.views.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.ui.utils.AlertUtil

class SettingFragment : PreferenceFragmentCompat()
        , SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        findPreference("p_shop_name").apply {
            summary = prefs.getString("p_shop_name", "Flex")
        }

        findPreference("p_shop_mail").apply {
            summary = prefs.getString("p_shop_mail", "")
        }

        findPreference("p_app_language").apply {
            summary = prefs.getString("p_app_language", "english")
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val app = context as MainActivity
        app.setTitle(R.string.setting)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val pref = findPreference(key)
        val origin = pref.summary.toString()
        pref.summary = sharedPreferences?.getString(key, origin)?.let {
            if (it.isEmpty()) {
                return@let origin
            } else {
                return@let it
            }
        }

        when (key) {
            "p_app_language" -> {
                context?.also {
                    AlertUtil.showToast(it, "Please restart application to take effect")
                }
            }
        }

    }


}