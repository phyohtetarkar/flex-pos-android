package com.jsoft.pos.ui.views

import android.arch.lifecycle.LiveData

interface ListViewModel<T> {

    val list: LiveData<List<T>>

}