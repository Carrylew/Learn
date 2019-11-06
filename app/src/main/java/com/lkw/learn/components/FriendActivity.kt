package com.lkw.learn.components

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.lkw.learn.R
import com.lkw.learn.adapter.EventAdapter
import com.lkw.learn.adapter.MyAdapter
import com.lkw.learn.components.event.AddEventActivity
import com.lkw.learn.components.friend.EditFriendActivity
import com.lkw.learn.db.AppDataBase
import com.lkw.learn.db.entity.EventEntity
import com.lkw.learn.db.entity.FriendEntity
import com.lkw.learn.model.Event
import com.lkw.learn.model.Person
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FriendActivity : AppCompatActivity() {
    lateinit var tvName: TextView
    lateinit var tvDesc: TextView
    lateinit var tvReceive: TextView
    lateinit var tvSend: TextView
    lateinit var bt_sure: Button
    lateinit var listView: ListView
    lateinit var toolbar: Toolbar
    lateinit var adapter: EventAdapter
    lateinit var data: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)
        data = intent.getSerializableExtra("friend") as Person
        initView()

    }

    override fun onResume() {
        super.onResume()
        queryFriend()
        queryEvent()
    }

    fun queryFriend() {
        val database = AppDataBase.getDatabase(this)
        val giftDao = database?.giftDao()
        val single = giftDao?.getFriendById(data.id)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<List<FriendEntity>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@FriendActivity, "查询好友信息失败！", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: List<FriendEntity>) {
                    if (t.size == 1) {
                        data = Person(t[0])
                        updateData()
                    }

                }

            })
    }

    fun queryEvent() {
        val database = AppDataBase.getDatabase(this)
        val dao = database?.eventDao()
        val single = dao?.getListByFriendId(data.id)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<List<EventEntity>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@FriendActivity, "查询好友随礼信息失败！", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: List<EventEntity>) {
                    val list = t.map {
                        Event(it)
                    }
                    adapter = EventAdapter(this@FriendActivity, list as MutableList<Event>)
                    adapter.listener = object : EventAdapter.OnEventDeleteClick {
                        override fun click(item: Event) {

                            val builder = AlertDialog.Builder(this@FriendActivity)
                            builder.setTitle("提示").setMessage("确认删除该条来往信息吗？")
                                .setPositiveButton(
                                    "是"
                                ) { _, _ ->
                                    val eventEntity = EventEntity()
                                    eventEntity.id = item.id
                                    deleteItem(eventEntity)
                                }.setNegativeButton(
                                    "否"
                                ) { _, _ ->

                                }.show()


                        }

                    }
                    listView.adapter = adapter
                }

            })
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
        bt_sure = findViewById(R.id.bt_sure)
        tvReceive = findViewById(R.id.tv_receive)
        listView = findViewById(R.id.listView)
        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        bt_sure.setOnClickListener {
            var intent = Intent(this, AddEventActivity::class.java)
            intent.putExtra("friend", data)
            startActivity(intent)
        }
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
                    deleteEventById()
                }.setNegativeButton(
                    "否"
                ) { _, _ ->

                }.show()
            }
        }
        return true
    }

    /**
     * step 2 : 删除随礼事件
     */
    private fun deleteItem(eventEntity: EventEntity) {
        val database = AppDataBase.getDatabase(this)
        val dao = database?.eventDao()
        val single = dao?.delete(eventEntity)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@FriendActivity, "删除失败", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: Int) {
                    queryFriend2()
                }

            })
    }

    /**
     * step 3 : 根据friendId 查询信息
     */
    private fun queryFriend2() {
        val database = AppDataBase.getDatabase(this)
        val dao = database?.eventDao()
        val single = dao?.getListByFriendId(data.id)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<List<EventEntity>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@FriendActivity, "查询好友随礼信息失败！", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: List<EventEntity>) {
                    var s_count = 0
                    var r_count = 0
                    t.forEach {
                        s_count += it.s_money
                        r_count += it.r_money
                    }
                    updateCount(s_count, r_count)
                }

            })
    }

    /**
     * step 4 : 求和
     */
    private fun updateCount(s_count: Int, r_count: Int) {
        val database = AppDataBase.getDatabase(this)
        val dao = database?.giftDao()
        val single = dao?.updateCount(data.id, s_count, r_count)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@FriendActivity, "更新求和信息失败", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: Int) {
                    Toast.makeText(this@FriendActivity, "成功删除一条数据", Toast.LENGTH_SHORT).show()
                    queryFriend()
                    queryEvent()
                }

            })
    }

    /**
     * 删除好友
     */
    private fun deleteFriend() {
        val friendEntity = FriendEntity()
        friendEntity.id = data.id
        val database = AppDataBase.getDatabase(this)
        val dao = database?.giftDao()
        val single = dao?.delete(friendEntity)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@FriendActivity, "删除好友成功", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: Int) {
                    Toast.makeText(this@FriendActivity, "删除好友成功", Toast.LENGTH_SHORT).show()
                    finish()
                }

            })
    }

    private fun deleteEventById() {
        val friendEntity = FriendEntity()
        friendEntity.id = data.id
        val database = AppDataBase.getDatabase(this)
        val dao = database?.eventDao()
        val single = dao?.delete(data.id)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }

                override fun onSuccess(t: Int) {
                    deleteFriend()
                }

            })
    }
}
