package com.jsoft.pos.ui.utils

interface ValidatorDelegate<in T> {

    fun validate(t: T?): Boolean

}