package com.jsoft.pos.ui.views.setting

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Environment
import com.jsoft.pos.FlexPosApplication
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class BackupViewModel(application: Application) : AndroidViewModel(application) {

    val backups = MutableLiveData<List<String>>()

    val restoreSuccess = MutableLiveData<Boolean>()
    val backupSuccess = MutableLiveData<Boolean>()

    @SuppressLint("SdCardPath")
    fun saveBackup() {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), "FlexPosBackups")
            val source = File("/data/data/com.jsoft.pos/databases/pos.db")

            if (source.exists()) {
                val format = SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH)
                val fileName = format.format(Date()).plus(".db")
                val outFile = File(dir, fileName)
                source.copyTo(outFile, true)
                backupSuccess.value = true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadBackups() {
        val dir = File(Environment.getExternalStorageDirectory(), "FlexPosBackups")
        if (!dir.exists()) {
            dir.mkdir()
        }

        backups.value = dir.listFiles().map { f -> f.name }
    }

    @SuppressLint("SdCardPath")
    fun restoreBackup(fileName: String) {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), "FlexPosBackups")
            val from = File(dir, fileName)
            val to = File("/data/data/com.jsoft.pos/databases/pos.db")

            if (from.exists()) {
                from.copyTo(to, true)
                restoreSuccess.value = true
                getApplication<FlexPosApplication>().also {
                    it.closeDb()
                }.also {
                    it.initDb()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}