package com.example.cantinhodecoracao.Util

import android.R.attr
import android.app.Activity
import android.util.Log
import android.widget.Toast
import java.io.*


class Directory {
    private var file: File;
    private var activity: Activity

    constructor(activity: Activity) {
        this.file = activity.filesDir
        this.activity = activity

        if (this.file.exists() == false) activity.filesDir.mkdirs()
    }

    fun pathExistsAndCreate(path: String): String {
        var file: File = File(this.file.path, File.separator + path)

        if (!file.exists()) file.mkdirs()
        return file.path
    }

    fun saveFile(path: String, fileName: String, value: String) {
        var file: File = File(this.pathExistsAndCreate(path), File.separator + fileName)

        try {
            if (!file.exists()) file.createNewFile()

            var outputStream = FileOutputStream(file, true)
            outputStream.write(value.toByteArray())
            outputStream.flush()
            outputStream.close()
        } catch (error: Exception) {
            Toast.makeText(activity, "Não foi possível armazenar arquivo ${fileName}", Toast.LENGTH_LONG).show()
        }
    }

    fun deleteFile(path: String, fileName: String): Boolean {
        var file: File = File(this.pathExistsAndCreate(path), File.separator + fileName)

        try {
            if (!file.exists()) return true
            file.delete()
        } catch (error: Exception) {
            return false
        }

        return true
    }

    fun readFile(path: String, fileName: String): String {
        var line: String = ""
        var file: File = File(this.pathExistsAndCreate(path), File.separator + fileName)

        if (!file.exists() || !file.isFile) return line

        try {
            val fileInputStream = FileInputStream(file)
            line = fileInputStream.bufferedReader().use(BufferedReader::readText)
        } catch (ex: FileNotFoundException) {
        } catch (ex: IOException) {
        }

        return line
    }
}