package com.jsoft.pos.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.jsoft.pos.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtil {

    fun writeImage(context: Context, uri: Uri?, name: String?): String? {

        var fos: FileOutputStream? = null

        try {

            val bmp = decodeImage(context, uri)

            val dir = context.getDir("item_image", Context.MODE_PRIVATE)

            name?.takeIf { it.isNotEmpty() }.apply {
                val file = File(dir, name)
                val deleted = file.delete()
            }

            if (!dir.exists()) {
                dir.mkdirs()
            }

            val format = SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH)
            val file = File(dir, String.format(Locale.ENGLISH, "%s.png", format.format(Date())))

            fos = FileOutputStream(file)

            bmp?.compress(Bitmap.CompressFormat.PNG, 100, fos)

            return file.name

        } catch (e: Exception) {
            e.printStackTrace()
            AlertUtil.showBigToast(context, "Error writing image")
        } finally {
            fos?.close()
        }

        return null

    }

    fun generateReceipt(context: Context, bitmap: Bitmap): Uri? {

        var fos: FileOutputStream? = null

        try {

            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)

            val format = SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH)
            val file = File(dir, String.format(Locale.ENGLISH, "%s.png", format.format(Date())))

            fos = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

            return Uri.fromFile(file)

        } catch (e: Exception) {
            e.printStackTrace()
            AlertUtil.showBigToast(context, "Error writing image")
        } finally {
            fos?.close()
        }

        return null

    }

    fun readImage(context: Context, name: String?): Bitmap? {

        if (!name.isNullOrEmpty()) {

            var fin: FileInputStream? = null

            try {
                val dir = context.getDir("item_image", Context.MODE_PRIVATE)
                val file = File(dir, name)

                fin = FileInputStream(file)

                return BitmapFactory.decodeStream(fin)

            } catch (e: Exception) {
                e.printStackTrace()
                AlertUtil.showBigToast(context, "Error reading image")
            } finally {
                fin?.close()
            }

        }

        val bitmapDrawable: BitmapDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.resources.getDrawable(R.drawable.ic_placeholder, null) as BitmapDrawable
        } else {
            context.resources.getDrawable(R.drawable.ic_placeholder) as BitmapDrawable
        }

        return bitmapDrawable.bitmap
    }

    private fun decodeImage(ctx: Context, uri: Uri?): Bitmap? {
        try {
            val optStart = BitmapFactory.Options()
            optStart.inJustDecodeBounds = true

            BitmapFactory.decodeStream(ctx.contentResolver.openInputStream(uri), null, optStart)

            val REQUIRED_SIZE = 240

            var width_tmp = optStart.outWidth
            var height_tmp = optStart.outHeight

            var scale = 1
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break
                }
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }

            val optEnd = BitmapFactory.Options()
            optEnd.inSampleSize = scale

            return BitmapFactory.decodeStream(ctx.contentResolver.openInputStream(uri), null, optEnd)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

}