package com.flex.pos.ui.utils

import android.support.v7.util.DiffUtil
import com.flex.pos.data.entity.*
import com.flex.pos.data.entity.Unit

class ServiceLocator {

    companion object {

        @JvmStatic
        fun <T> locate(clazz: Class<T>): T? {
            return clazz.newInstance()
        }

        @JvmStatic
        fun <T> locate(clazz: Class<T>, args: Any): T? {
            return null
        }

    }

}