package com.lkw.learn.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.lkw.learn.R
import com.lkw.learn.db.AppDataBase
import com.lkw.learn.model.SumModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.widget.TextView



class StatisticsActivity : AppCompatActivity() {
    lateinit var toolBar: Toolbar
    lateinit var rTv: TextView
    lateinit var sTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        initView()
        initEvent()
        queryStatistics()
    }

    private fun initView() {
        toolBar = findViewById(R.id.toolBar)
        rTv = findViewById(R.id.r_tv)
        sTv = findViewById(R.id.s_tv)
    }

    private fun initEvent() {
        toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun queryStatistics() {
        val database = AppDataBase.getDatabase(this)
        val dao = database?.eventDao()
        val single = dao?.statistics()
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<List<SumModel>> {
                override fun onSuccess(t: List<SumModel>) {
                    if (t.size == 1) {
                        val rTotal = t[0].rTotal
                        val sTotal = t[0].sTotal
                        rTv.text = rTotal.toString()
                        sTv.text = sTotal.toString()
                    }
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@StatisticsActivity, "查询统计信息失败", Toast.LENGTH_SHORT).show()
                }


            })
    }
}
