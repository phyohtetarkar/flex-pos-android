package com.jsoft.pos.data.utils

import android.arch.lifecycle.MutableLiveData
import android.databinding.BaseObservable
import android.databinding.Observable

class SearchMutableLiveData<T : BaseObservable> : MutableLiveData<T>() {

    private val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            value = value
        }
    }

    override fun setValue(value: T?) {
        super.setValue(value)

        value?.addOnPropertyChangedCallback(callback)
    }

}
