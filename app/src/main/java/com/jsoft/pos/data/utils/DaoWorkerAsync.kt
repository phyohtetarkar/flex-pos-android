package com.jsoft.pos.data.utils

import android.os.AsyncTask

class DaoWorkerAsync<T>(
        private val worker: (T) -> Boolean,
        private val onSuccess: () -> Unit,
        private val onError: () -> Unit
    ) : AsyncTask<T, Void, Boolean>() {

    override fun doInBackground(ts: Array<T>): Boolean {
        try {
            return worker(ts[0])
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            onSuccess()
        } else {
            onError()
        }
    }

}