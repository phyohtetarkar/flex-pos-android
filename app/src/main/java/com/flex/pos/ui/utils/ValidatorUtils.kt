package com.flex.pos.ui.utils

object ValidatorUtils {

    val NOT_EMPTY: (String?) -> Boolean = { !it.isNullOrBlank() }

    val VALID_PERCENTAGE: (Double?) -> Boolean = {
        when (it) {
            null -> false
            in 1.0..100.0 -> true
            else -> false
        }
    }

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
