package com.jsoft.pos.ui.utils

import android.content.Context
import java.util.*

class ContextWrapperUtil {

    companion object {
        fun create(newBase: Context?): Context? {
            val ary = Utils.getSelectedLanguage(newBase)
            val locale = Locale(ary[0], ary[1])
            Locale.setDefault(locale)
            val config = newBase?.resources?.configuration
            config?.setLocale(locale)

            return newBase?.createConfigurationContext(config)
        }
    }

}