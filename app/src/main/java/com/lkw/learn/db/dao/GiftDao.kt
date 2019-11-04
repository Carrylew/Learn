package com.lkw.learn.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.lkw.learn.db.entity.GiftEntity
import io.reactivex.Single

@Dao
interface GiftDao {
    companion object {
        const val tableName = "gift"
    }

    @Query("SELECT * FROM $tableName")
    fun getList(): Single<List<GiftEntity>>
}