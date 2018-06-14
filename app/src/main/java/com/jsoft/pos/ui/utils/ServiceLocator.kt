package com.jsoft.pos.ui.utils

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