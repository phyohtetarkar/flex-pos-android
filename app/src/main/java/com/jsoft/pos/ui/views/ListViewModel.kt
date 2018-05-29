package com.jsoft.pos.ui.views

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

interface ListViewModel<T>

interface SimpleListViewModel<T> : ListViewModel<T> {

    val list: LiveData<List<T>>

}

interface PagedListViewModel<T> : ListViewModel<T> {

    val list: LiveData<PagedList<T>>

}