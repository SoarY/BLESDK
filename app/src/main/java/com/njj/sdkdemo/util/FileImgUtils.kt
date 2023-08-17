package com.njj.sdkdemo.util

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.njj.sdkdemo.MyApplication
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

/**
 * NAME：YONG_
 * Created at: 2023/8/17 11
 * Describe:
 */
object FileImgUtils {

    private fun getCropImgName(isCrop: Boolean): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var fileName = ""
        fileName = if (isCrop) {
            "IMG_" + timeStamp + "_CROP.png"
        } else {
            "IMG_$timeStamp.png"
        }
        return fileName
    }

    fun getCustomDialBgPath(): String {
        var CUSTOM_DIAL_BG_PATH: String =
            MyApplication.context.getExternalFilesDir(null)!!.getAbsoluteFile()
                .toString() + File.separator + "capture" + File.separator + "CUSTBG"
        return CUSTOM_DIAL_BG_PATH
    }

    fun createImageFile(context: Context, isCrop: Boolean): File? {
        var fileName = getCropImgName(isCrop)

        val rootFile = File(MyApplication.context.getExternalFilesDir(null), "capture")
        if (!rootFile.exists())
            rootFile.mkdirs()

        val imgFile = File(rootFile.absolutePath + File.separator + fileName)
        return imgFile
    }

    fun createImageUri(context: Context, isCrop: Boolean): Uri? {
        var fileName = getCropImgName(isCrop)
        val imgFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName)
        // 通过 MediaStore API 插入file 为了拿到系统裁剪要保存到的uri（因为App没有权限不能访问公共存储空间，需要通过 MediaStore API来操作）
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DATA, imgFile.absolutePath)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        return uri
    }

    fun getUriToFile(context: Context, uri: Uri?): File? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val path = cursor.getString(columnIndex)
            cursor.close()
            return File(path)
        }
        return null
    }

    fun getSmallBitmap(bitmap: Bitmap, needWidth: Int, needHeight: Int): Bitmap? {
        val width = needWidth.toFloat() * 1.0f / bitmap.width.toFloat()
        val height = needHeight.toFloat() * 1.0f / bitmap.height.toFloat()
        val matrix = Matrix()
        matrix.postScale(width, height, 0.0f, 0.0f)
        val createBitmap = Bitmap.createBitmap(needWidth, needHeight, Bitmap.Config.RGB_565)
        Canvas(createBitmap).drawBitmap(bitmap, matrix, Paint())
        return createBitmap
    }

    fun saveBitmap(bitmap: Bitmap, str: String?): Boolean {
        val size = bitmap.width * bitmap.height * 2
        val file = File(str)
        return try {
            if (file.exists()) {
                file.delete()
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            val fileOutputStream = FileOutputStream(file)
            val allocate = ByteBuffer.allocate(size)
            val tmpBitmap = bitmap.copy(Bitmap.Config.RGB_565, false)
            tmpBitmap.copyPixelsToBuffer(allocate)
            fileOutputStream.write(allocate.array())
            fileOutputStream.close()
            true
        } catch (e2: Exception) {
            e2.printStackTrace()
            false
        }
    }

//    fun getSmallBitmap(mBitmap: Bitmap?, needWidth: Int, needHeight: Int): Bitmap? {
//        var bitmap = scaleBitmap(mBitmap, needWidth, needHeight)
//        val createBitmap = Bitmap.createBitmap(needWidth, needHeight, Bitmap.Config.RGB_565)
//        val canvas = Canvas(createBitmap)
//        val paint = Paint()
//        paint.color = Color.BLACK
//        paint.isAntiAlias = true
//        paint.shader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//        canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT)
//        canvas.drawRoundRect(
//            RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat()),
//            40f,
//            40f,
//            paint
//        )
//        return createBitmap
//    }

//    fun scaleBitmap(origin: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
//        if (origin == null)
//            return null
//        val height = origin.height
//        val width = origin.width
//        val scaleWidth = newWidth.toFloat() / width
//        val scaleHeight = newHeight.toFloat() / height
//        val matrix = Matrix()
//        matrix.postScale(scaleWidth, scaleHeight) // 使用后乘
//        return Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
//    }
}