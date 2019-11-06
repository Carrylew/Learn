package com.lkw.learn.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.lkw.learn.R
import com.lkw.learn.model.Person

class MyAdapter (var context:Context,var list:MutableList<Person>): BaseAdapter() {

    var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val view:View
        if (convertView == null){
             view = inflater.inflate(R.layout.list_item,null)
            val tv_word = view.findViewById<TextView>(R.id.tv_word)
            val tv_name = view.findViewById<TextView>(R.id.tv_name)
            holder = ViewHolder(tv_word, tv_name)
            view.tag = holder
        }else{
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        val word  = list[position].header
        val name  = list[position].name
        holder.tv_word.text = word
        holder.tv_name.text = name
        if(list[position].r_money > list[position].s_money){  //收 》 送
            holder.tv_name.setTextColor(Color.GREEN)
        }
        if(list[position].r_money < list[position].s_money){
            holder.tv_name.setTextColor(Color.RED)
        }
        if (position == 0){
            holder.tv_word.visibility = View.VISIBLE
        } else{
            val lastWord = list[position - 1].header
            if (word == lastWord) {
                holder.tv_word.visibility = View.GONE;
            } else {
                holder.tv_word.visibility = View.VISIBLE;
            }
        }
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

    class ViewHolder(val tv_word:TextView,val tv_name:TextView)
}