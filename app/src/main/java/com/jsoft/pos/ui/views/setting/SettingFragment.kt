package com.jsoft.pos.ui.views.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import com.jsoft.pos.MainActivity
import com.jsoft.pos.R
import com.jsoft.pos.ui.utils.AlertUtil
import com.jsoft.pos.ui.utils.LockHandler
import com.jsoft.pos.ui.utils.ServiceLocator
import com.jsoft.pos.ui.views.lock.EditLockFragment

class SettingFragment : PreferenceFragmentCompat()
        , SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        findPreference("p_shop_name").apply {
            summary = prefs.getString("p_shop_name", "Flex POS")
        }

        findPreference("p_shop_mail").apply {
            summary = prefs.getString("p_shop_mail", "mail for sending receipt")
        }

        // next version
        /*findPreference("p_app_language").apply {
            summary = prefs.getString("p_app_language", "english")
        }*/

        var code: String? = null

        findPreference("p_lock_code").apply {
            code = prefs.getString("p_lock_code", null)
            setOnPreferenceClickListener {
                val ft = fragmentManager?.beginTransaction()
                val prev = fragmentManager?.findFragmentByTag("lock")
                if (prev != null) {
                    ft?.remove(prev)
                }
                val frag = ServiceLocator.locate(EditLockFragment::class.java)
                frag?.show(ft, "lock")
                return@setOnPreferenceClickListener false
            }
        }

        findPreference("p_app_lock").apply {
            isEnabled = !code.isNullOrBlank()
        }

        findPreference("p_backup").setOnPreferenceClickListener {
            LockHandler.navigated(activity, true)
            return@setOnPreferenceClickListener false
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

        when (key) {
            "p_app_language" -> {
                context?.also {
                    AlertUtil.showToast(it, "Please restart application to take effect")
                }
            }

            "p_app_lock" -> {

            }

            "p_lock_code" -> {
                val code = sharedPreferences?.getString(key, null)
                findPreference("p_app_lock").apply {
                    isEnabled = !code.isNullOrBlank()
                }
            }

            else -> {
                val pref = findPreference(key)
                val origin = pref.summary.toString()
                pref.summary = sharedPreferences?.getString(key, origin)?.let {
                    if (it.isEmpty()) {
                        return@let origin
                    } else {
                        return@let it
                    }
                }
            }
        }

    }


}