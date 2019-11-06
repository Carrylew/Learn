package com.lkw.learn.db

import android.content.Context
import androidx.room.Database
import com.lkw.learn.db.entity.FriendEntity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lkw.learn.db.dao.EventDao
import com.lkw.learn.db.dao.FriendDao
import com.lkw.learn.db.entity.EventEntity

//import com.lkw.learn.db.dao.FriendDao

@Database(entities = [FriendEntity::class,EventEntity::class], version = 1)
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
    abstract fun giftDao(): FriendDao
    abstract fun eventDao(): EventDao

}