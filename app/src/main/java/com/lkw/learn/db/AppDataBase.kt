package com.lkw.learn.db

import android.content.Context
import androidx.room.Database
import com.lkw.learn.db.entity.GiftEntity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lkw.learn.db.dao.GiftDao

//import com.lkw.learn.db.dao.GiftDao

@Database(entities = [GiftEntity::class], version = 1)
abstract class AppDataBase :RoomDatabase(){

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase? {
            if (instance == null) {
                synchronized(AppDataBase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDataBase::class.java, "gift.sqlite"
                        ).build()
                    }
                }
            }
            return instance
        }
    }
    abstract fun giftDao(): GiftDao

}