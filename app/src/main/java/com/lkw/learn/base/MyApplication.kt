package com.lkw.learn.base

import android.app.Application
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import com.lkw.learn.services.PathService
import java.io.File

class MyApplication : Application() {

    override fun openOrCreateDatabase(name: String?, mode: Int, factory: SQLiteDatabase.CursorFactory?,
                                      errorHandler: DatabaseErrorHandler?): SQLiteDatabase {
        val db = PathService.dbFile
        return super.openOrCreateDatabase(db.absolutePath, mode, factory, errorHandler)
    }

    override fun getDatabasePath(name: String?): File {
        return PathService.dbFile
    }
}