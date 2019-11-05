package com.lkw.learn.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
class EventEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""
    @ColumnInfo(name = "title")
    var title: String = ""
    @ColumnInfo(name = "desc")
    var desc: String? = null
    @ColumnInfo(name = "r_money")
    var r_money: Int = 0
    @ColumnInfo(name = "s_money")
    var s_money: Int = 0
    @ColumnInfo(name = "friend_id")
    var friend_id: String = ""
}
