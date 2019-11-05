package com.lkw.learn.model

import com.lkw.learn.db.entity.EventEntity
import com.lkw.learn.db.entity.FriendEntity
import net.sourceforge.pinyin4j.PinyinHelper
import java.io.Serializable

class Event() : Serializable {

    constructor(entity: EventEntity) : this() {
        this.id = entity.id
        this.title = entity.title
        this.desc = entity.desc
        this.r_money = entity.r_money
        this.s_money = entity.s_money
        this.friend_id = entity.friend_id
    }

    var id: String = ""
    var title: String = ""
    var desc: String? = null
    var r_money: Int = 0
    var s_money: Int = 0
    var friend_id: String = ""

}