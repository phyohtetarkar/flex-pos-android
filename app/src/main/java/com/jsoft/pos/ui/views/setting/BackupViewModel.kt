package com.jsoft.pos.ui.views.setting

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SdCardPath")
class BackupViewModel(application: Application) : AndroidViewModel(application) {

    val backups = MutableLiveData<List<String>>()

    val restoreSuccess = MutableLiveData<Boolean>()
    val deleteSuccess = MutableLiveData<Boolean>()
    val backupSuccess = MutableLiveData<Boolean>()

    private val pathBackup = "FlexPos/.backup"
    private val dbSource: File by lazy { File("/data/data/com.jsoft.pos/databases") }
    private val imageSource: File by lazy { application.getDir("item_image", Context.MODE_PRIVATE) }
    private val receiptSource: File by lazy { application.getExternalFilesDir("receipts") }

    private val dbDir = "db"
    private val imgDir = "image"
    private val receiptDir = "receipts"

    fun saveBackup() {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), pathBackup)

            if (!dir.exists()) {
                dir.mkdirs()
            }
            val format = SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH)
            val outDir = File(dir, format.format(Date()))
            outDir.mkdir()

            if (dbSource.exists()) {
                val outDb = File(outDir, dbDir)
                outDb.mkdir()

                dbSource.listFiles().forEach {
                    val outFile = File(outDb, it.name).also { it.createNewFile() }
                    it.copyTo(outFile, true)
                }
            }

            if (imageSource.exists()) {
                val outImg = File(outDir, imgDir)
                outImg.mkdir()

                imageSource.listFiles().forEach {
                    val outFile = File(outImg, it.name).also { it.createNewFile() }
                    it.copyTo(outFile, true)
                }
            }

            if (receiptSource.exists()) {
                val outReceipt = File(outDir, receiptDir)
                outReceipt.mkdir()

                receiptSource.listFiles().forEach {
                    val outFile = File(outReceipt, it.name).also { it.createNewFile() }
                    it.copyTo(outFile, true)
                }
            }

            backupSuccess.value = true
        } catch (e: IOException) {
            e.printStackTrace()
            backupSuccess.value = false
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
            deleteSuccess.value = false
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

    fun restoreBackup(name: String?) {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), pathBackup)
            val bkp = File(dir, name)

            if (dbSource.exists()) {
                val dbBkp = File(bkp, dbDir)

                if (dbBkp.exists()) {
                    dbSource.listFiles().forEach { it.delete() }

                    dbBkp.listFiles().forEach {
                        val outFile = File(dbSource, it.name).also { it.createNewFile() }
                        it.copyTo(outFile, true)
                    }
                }
            }

            if (!imageSource.exists()) {
                imageSource.mkdirs()
            }

            val imgBkp = File(bkp, imgDir)

            if (imgBkp.exists()) {
                imageSource.listFiles().forEach { it.delete() }

                imgBkp.listFiles().forEach {
                    val outFile = File(imageSource, it.name).also { it.createNewFile() }
                    it.copyTo(outFile, true)
                }
            }

            val receiptBkp = File(bkp, receiptDir)

            if (receiptBkp.exists()) {
                receiptSource.listFiles().forEach { it.delete() }

                receiptBkp.listFiles().forEach {
                    val outFile = File(receiptSource, it.name).also { it.createNewFile() }
                    it.copyTo(outFile, true)
                }
            }

            restoreSuccess.value = true
        } catch (e: IOException) {
            e.printStackTrace()
            restoreSuccess.value = false
        }
    }

}