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
import com.lkw.learn.model.Person
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class EditFriendActivity : AppCompatActivity() {

    lateinit var etName :EditText
    lateinit var etDesc :EditText
    lateinit var btSure :Button
    lateinit var toolBar:Toolbar
    lateinit var data : Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_friend)
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
        val nameStr = etName.text.toString()
        val descStr = etDesc.text.toString()
        if (nameStr.isBlank()) {
            Toast.makeText(this, "好友姓名不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        val entity = FriendEntity()
        entity.id = data.id
        entity.name = nameStr
        entity.s_total_money = data.s_money
        entity.r_total_money = data.r_money
        entity.desc = descStr

        val database = AppDataBase.getDatabase(this)
        val giftDao = database?.giftDao()
        val single = giftDao?.update(entity)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@EditFriendActivity, "数据修改失败，可能是该好友已经存在", Toast.LENGTH_LONG).show()
                }

                override fun onSuccess(t: Int) {
                    print("lkw    ----------------------------------- $t")
                    Toast.makeText(this@EditFriendActivity, "修改成功", Toast.LENGTH_SHORT).show()
                    finish()
                }

            })
    }

    private fun initView() {
        etName = findViewById(R.id.et_name)
        etName.setText(data.name)
        etDesc = findViewById(R.id.et_desc)
        etDesc.setText(data.desc)
        btSure = findViewById(R.id.bt_sure)
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener{
            finish()
        }

    }
}
