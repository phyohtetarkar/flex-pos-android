package com.jsoft.es.ui.utils

interface ValidatorDelegate<in T> {

    fun validate(t: T?): Boolean

}