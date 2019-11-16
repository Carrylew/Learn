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

    @Query("SELECT * FROM $tableName where name like :keyword")
    fun getList(keyword:String): Single<List<FriendEntity>>

    @Query("SELECT * FROM $tableName where id =:id limit 1")
    fun getFriendById(id :String): Single<List<FriendEntity>>

    @Query("update $tableName set s_total_money = :s_totalMonet,r_total_money = :r_total_money where id =:id")
    fun updateCount(id :String,s_totalMonet:Int,r_total_money:Int):Single<Int>

    @Insert
    fun insert(entity: FriendEntity):Single<Long>

    @Delete
    fun delete(entity: FriendEntity):Single<Int>

    @Update
    fun update(entity: FriendEntity):Single<Int>

}