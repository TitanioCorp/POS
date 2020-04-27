package com.titaniocorp.pos.util

import android.content.Context
import android.content.ContextWrapper
import java.io.File

object FileUtil {
    fun getDirectory(applicationContext: Context): Pair<Boolean?, File?>{
        val contextWrapper = ContextWrapper(applicationContext)
        val directory = contextWrapper.getDir("temp_files", Context.MODE_PRIVATE)
        if (!directory.exists()) {
            val createFolder = directory.mkdirs()
            if (!createFolder) {
                return Pair(false, directory)
            }
        }
        return Pair(true, directory)
    }

    fun deleteDirectory(applicationContext: Context): Boolean {
        val contextWrapper = ContextWrapper(applicationContext)
        val directory = contextWrapper.getDir("temp_files", Context.MODE_PRIVATE)

        if (directory.exists()) {
            directory.listFiles()?.forEach { it.delete() }
            directory.delete()

            if(directory.exists()){
                return false
            }

            return true
        }
        return true
    }

    fun createFile(directory: File, filename: String, extension: String): File {
        return  File.createTempFile(
            "${filename}_",
            ".$extension",
            directory
        )
    }
}