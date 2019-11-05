package com.lkw.learn.components.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.lkw.learn.R
import com.lkw.learn.db.AppDataBase
import com.lkw.learn.db.entity.FriendEntity
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*

class AddFriendActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etDesc: EditText
    lateinit var btSure: Button
    lateinit var toolBar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
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
        val nameStr = etName.text.toString()
        val descStr = etDesc.text.toString()
        if (nameStr.isBlank()) {
            Toast.makeText(this, "好友姓名不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        val entity = FriendEntity()
        entity.id = UUID.randomUUID().toString()
        entity.name = nameStr
        entity.s_total_money = 0
        entity.r_total_money = 0
        entity.desc = descStr

        val database = AppDataBase.getDatabase(this)
        val giftDao = database?.giftDao()
        val single = giftDao?.insert(entity)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Long> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@AddFriendActivity, "好友添加失败，可能是该好友已经存在!", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(t: Long) {
//                    val builder = AlertDialog.Builder(this@AddFriendActivity)
//                    builder.setTitle("提示").setMessage("成功插入一条新数据，是否继续添加").setPositiveButton(
//                        "是"
//                    ) { _, _ -> etName.setText("")
//                        etSend.setText("")
//                        etReceive.setText("")
//                        etDesc.setText("")
//                    }.setNegativeButton(
//                        "否"
//                    ) { _, _ -> finish() }.show()
                    finish()
                }

            })
    }

    private fun initView() {
        etName = findViewById(R.id.et_name)
        etDesc = findViewById(R.id.et_desc)
        btSure = findViewById(R.id.bt_sure)
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)


    }
}
