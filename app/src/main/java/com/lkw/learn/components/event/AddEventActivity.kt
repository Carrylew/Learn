package com.lkw.learn.components.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.lkw.learn.R
import com.lkw.learn.db.AppDataBase
import com.lkw.learn.db.entity.EventEntity
import com.lkw.learn.db.entity.FriendEntity
import com.lkw.learn.model.Event
import com.lkw.learn.model.Person
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class AddEventActivity : AppCompatActivity() {

    lateinit var etTitle: EditText
    lateinit var etMoney: EditText
    lateinit var rb_send: RadioButton
    lateinit var rb_receive: RadioButton
    lateinit var btSure: Button
    lateinit var toolBar: Toolbar
    lateinit var data:Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        data = intent.getSerializableExtra("friend") as Person
        initView()
        initEvent()
    }

    private fun initEvent() {
        toolBar.setNavigationOnClickListener {
            finish()
        }
        btSure.setOnClickListener {
            commit()
        }
    }

    private fun commit() {
        val titleStr = etTitle.text.toString()
        if (titleStr.isBlank()) {
            Toast.makeText(this, "原因不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        val moneyStr = etMoney.text.toString()
        try {
            moneyStr.toInt()
        } catch (e: Exception) {
            Toast.makeText(this, "输入金额无法转换为数字", Toast.LENGTH_SHORT).show()
            return
        }
        val eventEntity = EventEntity()
        eventEntity.id = UUID.randomUUID().toString()
        eventEntity.title = etTitle.text.toString()
        if (rb_receive.isChecked) {
            eventEntity.r_money = etMoney.text.toString().toInt()
            eventEntity.s_money = 0
        } else {
            eventEntity.s_money = etMoney.text.toString().toInt()
            eventEntity.r_money = 0
        }
        eventEntity.friend_id = data.id
        insertEvent(eventEntity)
    }
    /**
     * step 2 : 添加随礼事件 id关联friendId
     */
    private fun insertEvent(eventEntity: EventEntity) {
        val database = AppDataBase.getDatabase(this)
        val dao = database?.eventDao()
        val single = dao?.insert(eventEntity)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Long> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@AddEventActivity, "随礼信息添加失败！", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: Long) {
                    queryFriend(eventEntity)
                }

            })
    }

    /**
     * step 3 : 根据friendId 查询信息
     */
    private fun queryFriend(eventEntity: EventEntity) {
        val database = AppDataBase.getDatabase(this)
        val dao = database?.eventDao()
        val single = dao?.getListByFriendId(eventEntity.friend_id)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<List<EventEntity>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@AddEventActivity, "查询好友随礼信息失败！", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: List<EventEntity>) {
                    var  s_count = 0
                    var  r_count = 0
                    t.forEach {
                        s_count += it.s_money
                        r_count += it.r_money
                    }
                    updateCount(s_count,r_count)
                }

            })
    }

    /**
     * step 4 : 求和
     */
    private fun updateCount(s_count:Int,r_count:Int) {
        val database = AppDataBase.getDatabase(this)
        val dao = database?.giftDao()
        val single = dao?.updateCount(data.id,s_count,r_count)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@AddEventActivity, "更新求和信息失败", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: Int) {
                    Toast.makeText(this@AddEventActivity, "成功", Toast.LENGTH_SHORT).show()
                    finish()
                }

            })
    }

    private fun initView() {
        etTitle = findViewById(R.id.et_title)
        etMoney = findViewById(R.id.et_money)
        rb_send = findViewById(R.id.rb_send)
        rb_receive = findViewById(R.id.rb_receive)
        btSure = findViewById(R.id.bt_sure)
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
    }
}
