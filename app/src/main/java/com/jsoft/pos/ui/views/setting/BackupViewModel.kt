package com.jsoft.pos.ui.views.setting

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class BackupViewModel(application: Application) : AndroidViewModel(application) {

    val backups = MutableLiveData<List<String>>()

    val restoreSuccess = MutableLiveData<Boolean>()
    val deleteSuccess = MutableLiveData<Boolean>()
    val backupSuccess = MutableLiveData<Boolean>()

    private val pathBackup = "FlexPos/.backup"
    @SuppressLint("SdCardPath")
    private val source = "/data/data/com.jsoft.pos/databases"

    fun saveBackup() {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), pathBackup)
            val source = File(source)

            if (!dir.exists()) {
                dir.mkdirs()
            }

            if (source.exists()) {
                val format = SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH)
                val outDir = File(dir, format.format(Date()))
                outDir.mkdirs()

                source.listFiles().forEach {
                    val outFile = File(outDir, it.name).also { it.createNewFile() }
                    it.copyTo(outFile, true)
                }
                backupSuccess.value = true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteBackup(name: String?) {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), pathBackup)
            val file = File(dir, name)


            if (file.exists()) {
                deleteSuccess.value = file.deleteRecursively()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadBackups() {
        val dir = File(Environment.getExternalStorageDirectory(), pathBackup)

        if (dir.exists()) {
            backups.value = dir.list().orEmpty().sortedDescending()
        } else {
            backups.value = emptyList()
        }
    }

    @SuppressLint("SdCardPath")
    fun restoreBackup(name: String?) {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), pathBackup)
            val from = File(dir, name)
            val to = File(source)

            if (from.exists()) {
                to.listFiles().forEach { it.delete() }
                from.listFiles().forEach {
                    val outFile = File(to, it.name).also { it.createNewFile() }
                    it.copyTo(outFile, true)
                }
                restoreSuccess.value = true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
        }
    }

}