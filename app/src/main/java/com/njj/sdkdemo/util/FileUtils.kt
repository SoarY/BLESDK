package com.soar.cloud.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * NAME：YONG_
 * Created at: 2019/1/7
 * Describe:
 */
object FileUtils {

    fun uriToFile(context: Context, uri: Uri?): File? {
        if (uri == null)
            return null
        var file: File? = null
        if (uri.scheme != null) {
            if (uri.scheme == ContentResolver.SCHEME_FILE && uri.path != null) {
                //此uri为文件，并且path不为空(保存在沙盒内的文件可以随意访问，外部文件path则为空)
                file = File(uri.path)
            } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                //此uri为content类型，将该文件复制到沙盒内
                val resolver = context.contentResolver
                @SuppressLint("Recycle") val cursor = resolver.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex==-1)
                        return null
                    val fileName = cursor.getString(columnIndex)
                    try {
                        val inputStream = resolver.openInputStream(uri)
                        if (context.externalCacheDir != null) {
                            //该文件放入cache缓存文件夹中
                            val cache = File(context.externalCacheDir, fileName)
                            val fileOutputStream = FileOutputStream(cache)
                            if (inputStream != null) {
                                //上面的copy方法在低版本的手机中会报java.lang.NoSuchMethodError错误，使用原始的读写流操作进行复制
                                val len = ByteArray(Math.min(inputStream.available(), 1024 * 1024))
                                var read: Int
                                while (inputStream.read(len).also { read = it } != -1) {
                                    fileOutputStream.write(len, 0, read)
                                }
                                file = cache
                                fileOutputStream.close()
                                inputStream.close()
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return file
    }
}