package com.jsoft.es.ui.utils

object ValidatorUtils {

    val NOT_EMPTY: (String?) -> Boolean  = { !it.isNullOrBlank() }

    fun <T> validate(t: T, validator: (T?) -> Boolean, errorHandler: () -> Unit): Boolean {
        val valid = validator(t)
        if (!valid) {
            errorHandler()
        }
        return valid
    }

    fun <T> isValid(t: T, validator: (T?) -> Boolean): Boolean {
        return validator(t)
    }

}
