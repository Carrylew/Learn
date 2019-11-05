package com.lkw.learn.model

import com.lkw.learn.db.entity.FriendEntity
import net.sourceforge.pinyin4j.PinyinHelper
import java.io.Serializable

class Person(giftEntity: FriendEntity):Serializable{
    var header: String = ""
    var id: String = ""
    var name: String = ""
    var desc: String? = null
    var r_money: Int = 0
    var s_money: Int = 0
    var pinyin:String =""
    init {
        this.id = giftEntity.id
        this.name = giftEntity.name
        this.desc = giftEntity.desc
        this.r_money  =giftEntity.r_total_money
        this.s_money = giftEntity.s_total_money

        var hanzi = giftEntity.name
        if("" != hanzi) {
            val charArray = hanzi.toCharArray()
            var sb = StringBuffer()
            charArray.forEach {
               val stringArray =  PinyinHelper.toHanyuPinyinStringArray(it)
                if(null != stringArray) {
                    sb.append(stringArray[0])
                }
            }


            if(sb.isNotEmpty()) {
                this.pinyin = sb.toString().toUpperCase()
                this.header = this.pinyin.substring(0,1)
            }else{
                this.header = "#"
            }
        }

    }


}