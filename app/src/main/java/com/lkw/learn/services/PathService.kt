package com.lkw.learn.services

import android.os.Environment
import java.io.File

object PathService {
    const val DIC_ROOT = "SuiSui"
    const val FILE_DB = "data.sqlite"
    //创建或者获取File
    private fun getOrMakeDir(file: File): File {
        return if (file.exists() || file.mkdirs()) {
            file
        } else file
    }

    private val rootDir: File
        get() {
            return getOrMakeDir(
                File(
                    Environment.getExternalStorageDirectory().absolutePath,
                    DIC_ROOT
                )
            )
        }

    val dbFile: File
        get() {
            return File(rootDir, FILE_DB)
        }
}