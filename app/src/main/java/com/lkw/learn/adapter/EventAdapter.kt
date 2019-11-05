package com.lkw.learn.adapter

import android.content.Context
import android.graphics.Color
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.lkw.learn.R
import com.lkw.learn.model.Event
import com.lkw.learn.model.Person

class EventAdapter (var context:Context, var list:MutableList<Event>): BaseAdapter() {


    var inflater: LayoutInflater = LayoutInflater.from(context)
    var listener:  OnEventDeleteClick?=null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val view:View
        if (convertView == null){
             view = inflater.inflate(R.layout.event_list_item,null)
            val tv_title = view.findViewById<TextView>(R.id.item_title)
            val tv_desc = view.findViewById<TextView>(R.id.item_desc)
            val tv_money = view.findViewById<TextView>(R.id.item_money)
            val ib_delete = view.findViewById<ImageButton>(R.id.item_delete)
            ib_delete.setOnClickListener {
                listener?.click(list[position])
            }
            holder = ViewHolder(tv_title, tv_desc,tv_money,ib_delete)
            view.tag = holder
        }else{
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        val title  = "结婚"
        val desc  = "婚礼纪赠送的"
        val money = "收到：200"

        holder.tv_title.text = title
        holder.tv_desc.text = desc
        holder.tv_money.text = money
        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    class ViewHolder(val tv_title:TextView,val tv_desc:TextView,val tv_money:TextView,val ib_delete:ImageButton)

    public interface OnEventDeleteClick{
         fun click(item:Event)
    }
}