package com.example.chat.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageSaver {

    suspend fun saveImageToGallery(
        context: Context,
        imageUrl: String,
        titlePrefix: String = "StudyX_AI"
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val bytes = if (imageUrl.startsWith("data:")) {
                val commaIndex = imageUrl.indexOf(",")
                val base64Str = if (commaIndex != -1) imageUrl.substring(commaIndex + 1) else imageUrl
                android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT)
            } else {
                val client = OkHttpClient()
                val request = Request.Builder().url(imageUrl).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Failed to download image (HTTP ${response.code})"))
                }
                response.body?.bytes()
                    ?: return@withContext Result.failure(Exception("Empty image data received"))
            }

            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                ?: return@withContext Result.failure(Exception("Failed to decode image data"))

            val filename = "${titlePrefix}_${System.currentTimeMillis()}.png"
            val resolver = context.contentResolver

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/StudyX")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return@withContext Result.failure(Exception("Failed to create MediaStore entry"))

                resolver.openOutputStream(imageUri).use { outStream: OutputStream? ->
                    if (outStream == null) {
                        return@withContext Result.failure(Exception("Failed to open output stream"))
                    }
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                }

                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)

                Result.success(imageUri)
            } else {
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val appDir = File(picturesDir, "StudyX").apply { mkdirs() }
                val imageFile = File(appDir, filename)

                FileOutputStream(imageFile).use { outStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                }

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                }
                val insertedUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: Uri.fromFile(imageFile)

                Result.success(insertedUri)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
