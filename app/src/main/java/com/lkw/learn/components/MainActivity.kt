package com.lkw.learn.components

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.lkw.learn.R
import com.lkw.learn.base.BaseActivity
import com.lkw.learn.db.AppDataBase
import com.lkw.learn.db.entity.GiftEntity
import com.lkw.learn.views.MyAdapter
import com.lkw.learn.views.Person
import com.lkw.learn.views.WordNav
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar


class MainActivity : BaseActivity() {

    lateinit var listView: ListView
    lateinit var wordNav: WordNav
    lateinit var toolbar: Toolbar
    lateinit var tvBig :TextView
    var list: MutableList<Person> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wordNav = findViewById(R.id.wordNav)
        listView = findViewById(R.id.listView)
        toolbar = findViewById(R.id.toolBar)
        tvBig = findViewById(R.id.tv_big)
        setSupportActionBar(toolbar)
        wordNav.setListener { word ->
            updateList(word)
        }
        listView.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, EditActivity::class.java)
            intent.putExtra("data", list[position])
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        query()
    }

    private fun updateList(word: String?) {
        for (index in list.indices) {
            val person = list[index]
            val header = person.header
            if (word == header) {
                listView.setSelection(index)
            }
        }
        if(word == null){
            tvBig.visibility = View.GONE
            tvBig.text = ""
        }else{
            tvBig.visibility = View.VISIBLE
            tvBig.text = word
        }

    }


    fun query() {
        val database = AppDataBase.getDatabase(this)
        val giftDao = database?.giftDao()
        val single = giftDao?.getList()
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<List<GiftEntity>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onSuccess(t: List<GiftEntity>) {
                    if (t.isNotEmpty()) {
                        list.clear()
                        t.forEach {
                            list.add(Person(it))
                        }
                        list.sortWith(Comparator { o1, o2 -> o1.pinyin.compareTo(o2.pinyin) })
                        val adapter = MyAdapter(this@MainActivity, list)
                        listView.adapter = adapter
                        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
                            override fun onScroll(
                                view: AbsListView?,
                                firstVisibleItem: Int,
                                visibleItemCount: Int,
                                totalItemCount: Int
                            ) {
                                wordNav.setTouchIndex(list[firstVisibleItem].header)
                            }

                            override fun onScrollStateChanged(
                                view: AbsListView?,
                                scrollState: Int
                            ) {
                            }

                        })
                    }

                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.add -> {
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}
