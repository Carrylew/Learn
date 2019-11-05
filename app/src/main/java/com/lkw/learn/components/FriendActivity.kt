package com.lkw.learn.components

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.lkw.learn.R
import com.lkw.learn.adapter.EventAdapter
import com.lkw.learn.components.friend.EditFriendActivity
import com.lkw.learn.model.Event
import com.lkw.learn.model.Person

class FriendActivity : AppCompatActivity() {
    lateinit var tvName: TextView
    lateinit var tvDesc: TextView
    lateinit var tvReceive: TextView
    lateinit var tvSend: TextView
    lateinit var listView: ListView
    lateinit var toolbar: Toolbar
    lateinit var adapter: EventAdapter
    lateinit var data :Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)
        data = intent.getSerializableExtra("friend") as Person
        initView()
        updateData()
    }

    private fun updateData() {
        tvName.text = data.name
        tvDesc.text = data.desc
        tvSend.text = data.s_money.toString()
        tvReceive.text = data.r_money.toString()
    }

    private fun initView() {
        tvName = findViewById(R.id.tv_name)
        tvDesc = findViewById(R.id.tv_desc)
        tvSend = findViewById(R.id.tv_send)
        tvReceive = findViewById(R.id.tv_receive)
        listView = findViewById(R.id.listView)
        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        adapter = EventAdapter(this, arrayListOf(Event(), Event(), Event(), Event()))
        adapter.listener = object : EventAdapter.OnEventDeleteClick {
            override fun click(item: Event) {
                Toast.makeText(this@FriendActivity,"删除一条数据",Toast.LENGTH_SHORT).show()
            }

        }
        listView.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.edit -> {
                var intent = Intent(this, EditFriendActivity::class.java)
                intent.putExtra("friend", data)
                startActivity(intent)
            }
            R.id.delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("提示").setMessage("好友删除后，该好友的来往信息也将全部删除。确认要删除吗？").setPositiveButton(
                    "是"
                ) { _, _ ->
                }.setNegativeButton(
                    "否"
                ) { _, _ ->

                }.show()
            }
        }
        return true
    }
}
