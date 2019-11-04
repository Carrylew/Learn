package com.lkw.learn.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lkw.learn.db.entity.GiftEntity
import io.reactivex.Single
import androidx.room.Update
import androidx.room.Delete



@Dao
interface GiftDao {
    companion object {
        const val tableName = "gift"
    }

    @Query("SELECT * FROM $tableName")
    fun getList(): Single<List<GiftEntity>>

    @Insert
    fun insert(entity: GiftEntity):Single<Long>

    @Delete
    fun delete(entity: GiftEntity):Single<Int>

    @Update
    fun update(entity: GiftEntity):Single<Int>

}