package com.lkw.learn.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lkw.learn.db.entity.FriendEntity
import io.reactivex.Single
import androidx.room.Update
import androidx.room.Delete
@Dao
interface EventDao {
    companion object {
        const val tableName = "event"
    }

}