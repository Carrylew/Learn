package com.lkw.learn.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.lkw.learn.R
import com.lkw.learn.db.AppDataBase
import com.lkw.learn.db.entity.GiftEntity
import com.lkw.learn.views.Person
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*

class EditActivity : AppCompatActivity() {

    lateinit var etName :EditText
    lateinit var etSend :EditText
    lateinit var etReceive :EditText
    lateinit var etDesc :EditText
    lateinit var btSure :Button
    lateinit var btDel :Button
    lateinit var toolBar:Toolbar
    lateinit var data : Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        data = intent.getSerializableExtra("data") as Person
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
        btDel.setOnClickListener {
            val builder = AlertDialog.Builder(this@EditActivity)
            builder.setTitle("提示").setMessage("数据删除后无法恢复，确认要删除吗？").setPositiveButton(
                "是"
            ) { _, _ -> delete()
            }.setNegativeButton(
                "否"
            ) { _, _ ->  }.show()
        }
    }

    private fun delete() {

        val entity = GiftEntity()
        entity.id = data.id
        entity.name = data.name
        entity.s_money = data.s_money
        entity.r_money = data.r_money
        entity.desc = data.desc

        val database = AppDataBase.getDatabase(this)
        val giftDao = database?.giftDao()
        val single = giftDao?.delete(entity)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@EditActivity, "数据删除失败", Toast.LENGTH_LONG).show()
                }

                override fun onSuccess(t: Int) {
                    Toast.makeText(this@EditActivity, "数据删除成功", Toast.LENGTH_LONG).show()
                    finish()
                }

            })
    }

    private fun commit() {
        val nameStr = etName.text.toString()
        val sendStr = etSend.text.toString()
        val receiveStr = etReceive.text.toString()
        val descStr = etDesc.text.toString()
        if (nameStr.isBlank()) {
            Toast.makeText(this, "好友姓名不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            sendStr.toInt()
            receiveStr.toInt()
        } catch (e: Exception) {
            Toast.makeText(this, "输入金额无法转换为数字", Toast.LENGTH_SHORT).show()
            return
        }
        val entity = GiftEntity()
        entity.id = data.id
        entity.name = nameStr
        entity.s_money = sendStr.toInt()
        entity.r_money = receiveStr.toInt()
        entity.desc = descStr

        val database = AppDataBase.getDatabase(this)
        val giftDao = database?.giftDao()
        val single = giftDao?.update(entity)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@EditActivity, "数据修改失败，可能是该好友已经存在", Toast.LENGTH_LONG).show()
                }

                override fun onSuccess(t: Int) {
                    print("lkw    ----------------------------------- $t")
                    Toast.makeText(this@EditActivity, "修改成功", Toast.LENGTH_SHORT).show()
                    finish()
                }

            })
    }

    private fun initView() {
        etName = findViewById(R.id.et_name)
        etName.setText(data.name)
        etSend = findViewById(R.id.et_send)
        etSend.setText(data.s_money.toString())
        etReceive = findViewById(R.id.et_receive)
        etReceive.setText(data.r_money.toString())
        etDesc = findViewById(R.id.et_desc)
        etDesc.setText(data.desc)
        btSure = findViewById(R.id.bt_sure)
        btDel = findViewById(R.id.bt_del)
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener{
            finish()
        }

    }
}
