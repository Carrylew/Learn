package com.lkw.learn.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lkw.learn.db.entity.FriendEntity
import io.reactivex.Single
import androidx.room.Update
import androidx.room.Delete



@Dao
interface FriendDao {
    companion object {
        const val tableName = "friend"
    }

    @Query("SELECT * FROM $tableName")
    fun getList(): Single<List<FriendEntity>>

    @Insert
    fun insert(entity: FriendEntity):Single<Long>

    @Delete
    fun delete(entity: FriendEntity):Single<Int>

    @Update
    fun update(entity: FriendEntity):Single<Int>

}