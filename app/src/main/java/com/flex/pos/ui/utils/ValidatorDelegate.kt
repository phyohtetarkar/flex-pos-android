package com.flex.pos.ui.utils

interface ValidatorDelegate<in T> {

    fun validate(t: T?): Boolean

}