package com.lkw.learn.components

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.TextView
import com.lkw.learn.R
import com.lkw.learn.base.BaseActivity
import com.lkw.learn.db.AppDataBase
//import com.lkw.learn.db.AppDataBase
import com.lkw.learn.db.entity.GiftEntity
import com.lkw.learn.services.PathService
import com.lkw.learn.views.MyAdapter
import com.lkw.learn.views.Person
import com.lkw.learn.views.WordNav
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MainActivity : BaseActivity() {

    lateinit var listView: ListView
    lateinit var list: MutableList<Person>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val wordNav = findViewById<WordNav>(R.id.wordNav)
//        listView = findViewById(R.id.listView)
//        wordNav.setListener { word ->
//            updateList(word)
//        }
//        initData()
//        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
//            override fun onScroll(view: AbsListView?, firstVisibleItem: Int,visibleItemCount: Int,totalItemCount: Int ) {
//                wordNav.setTouchIndex(list[firstVisibleItem].header)
//            }
//
//            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
//            }
//
//        })
        test()
    }


    private fun initData() {
        list = ArrayList()
        list.add(Person("阿猫", "AM"))
        list.add(Person("阿狗", "AG"))
        list.add(Person("白云", "BY"))
        list.add(Person("鞠杨", "JY"))
        list.add(Person("朱红林", "ZHL"))
        list.add(Person("石望急", "SWJ"))
        list.add(Person("孙杰", "SJ"))
        list.add(Person("康达", "KD"))
        list.add(Person("苏川", "SC"))
        list.add(Person("何强", "XKM"))
        list.add(Person("赵想君", "ZXJ"))
        list.add(Person("王文兰", "WWL"))
        list.add(Person("邓双文", "DSW"))
        list.add(Person("罗朝奎", "LCK"))
        list.add(Person("何迎辉", "HYH"))
        list.add(Person("陈坦荣", "CTR"))
        list.add(Person("李晓峰", "LXF"))
        list.add(Person("刘然", "LR"))
        list.add(Person("叶超", "YC"))
        list.add(Person("刘柯为", "LKW"))
        list.add(Person("孙燕玲", "SYL"))
        list.add(Person("唐毅", "TY"))
        list.add(Person("邱波", "QB"))
        list.add(Person("谢文东", "XWD"))
        list.add(Person("汤禅军", "TCJ"))
        list.add(Person("周玉森", "ZYS"))
        list.add(Person("张建民", "ZJM"))
        list.add(Person("肖云", "XY"))
        list.add(Person("陶雪艳", "TXY"))

        list.sortWith(Comparator { o1, o2 -> o1.pinyin.compareTo(o2.pinyin) })
        val adapter = MyAdapter(this, list)
        listView.adapter = adapter
    }


    private fun updateList(word: String?) {
        for (index in list.indices) {
            val person = list[index]
            val header = person.header
            if (word == header){
                listView.setSelection(index)
            }
        }
    }

    fun test(): Unit {
        val database = AppDataBase.getDatabase(this)
        val giftDao = database?.giftDao()
        val single = giftDao?.getList()
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object :SingleObserver<List<GiftEntity>> {
                override fun onSubscribe(d: Disposable) {
                    println("lkw------------onSubscribe")
                }

                override fun onError(e: Throwable) {
                    println("lkw------------error")
                }

                override fun onSuccess(t: List<GiftEntity>) {
                    println("lkw------------success")
                }

            })
    }
}
