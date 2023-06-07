package com.example.aksacarma.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}


fun uriToFile(selectedImage: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val localFile = createTempFile(context)
    val inputStream = contentResolver.openInputStream(selectedImage) as InputStream
    val outputStream: OutputStream = FileOutputStream(localFile)
    val buf = ByteArray(1024)
    var len: Int

    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return localFile
}

fun reduceImageSize(file: File): File {
    val imageCompress = BitmapFactory.decodeFile(file.path)
    var imageCompressQuality = 100
    var streamLength: Int

    do {
        val outputStream = ByteArrayOutputStream()
        imageCompress.compress(Bitmap.CompressFormat.JPEG, imageCompressQuality, outputStream)
        val bmpConvert = outputStream.toByteArray()
        streamLength = bmpConvert.size
        imageCompressQuality -= 5
    } while (streamLength > 1000000)

    imageCompress.compress(Bitmap.CompressFormat.JPEG, imageCompressQuality, FileOutputStream(file))
    return file
}
