package com.jsoft.pos.ui.utils

import android.content.Context
import android.support.v7.preference.PreferenceManager

class Utils {

    companion object {
        @JvmStatic
        fun getSelectedLanguage(context: Context?): Array<String> {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)

            return when (pref.getString("p_app_language", "english")) {
                "myanmar(unicode)" -> arrayOf("my", "MM")
                "myanmar(zawgyi)" -> arrayOf("my", "ZG")
                else -> arrayOf("en", "US")
            }
        }
    }

}