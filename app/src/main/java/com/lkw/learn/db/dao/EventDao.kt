package com.lkw.learn.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lkw.learn.db.entity.FriendEntity
import io.reactivex.Single
import androidx.room.Update
import androidx.room.Delete
import com.lkw.learn.db.entity.EventEntity

@Dao
interface EventDao {
    companion object {
        const val tableName = "event"
    }
    @Insert
    fun insert(entity: EventEntity):Single<Long>

    @Query("SELECT * FROM $tableName where friend_id = :id")
    fun getListByFriendId(id:String): Single<List<EventEntity>>

    @Delete
    fun delete(entity: EventEntity):Single<Int>

    @Query("DELETE FROM $tableName where friend_id = :id")
    fun delete(id:String):Single<Int>
}