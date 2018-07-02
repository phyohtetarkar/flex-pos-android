package com.flex.pos.ui.utils

object ValidatorUtils {

    val VALID_PERCENTAGE: (Double?) -> Boolean = {
        when (it) {
            null -> false
            in 1.0..100.0 -> true
            else -> false
        }
    }

    fun <T> isValid(t: T, validator: (T?) -> Boolean): Boolean {
        return validator(t)
    }

}
