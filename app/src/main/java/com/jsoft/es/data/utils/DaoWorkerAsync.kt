package com.jsoft.es.data.utils

import android.os.AsyncTask

class DaoWorkerAsync<T>(
        private val worker: (T) -> Unit,
        private val onError: (Exception) -> Unit
    ) : AsyncTask<T, Void, Void>() {

    override fun doInBackground(ts: Array<T>): Void? {
        try {
            worker(ts[0])
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e)
        }

        return null
    }

}