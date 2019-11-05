package com.lkw.learn.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friend")
class FriendEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""
    @ColumnInfo(name = "name")
    var name: String = ""
    @ColumnInfo(name = "desc")
    var desc: String? = null
    @ColumnInfo(name = "r_total_money")
    var r_total_money: Int = 0
    @ColumnInfo(name = "s_total_money")
    var s_total_money: Int = 0
}