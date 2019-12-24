package com.lkw.learn.components

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.lkw.learn.R
import com.lkw.learn.base.BaseActivity
import com.lkw.learn.db.AppDataBase
import com.lkw.learn.db.entity.FriendEntity
import com.lkw.learn.adapter.MyAdapter
import com.lkw.learn.model.Person
import com.lkw.learn.views.WordNav
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.lkw.learn.components.friend.AddFriendActivity
import java.lang.Exception


class MainActivity : BaseActivity() {

    lateinit var listView: ListView
    lateinit var wordNav: WordNav
    lateinit var toolbar: Toolbar
    lateinit var tvBig: TextView
    lateinit var searchView: SearchView
    lateinit var root:LinearLayout
    var list: MutableList<Person> = ArrayList()
    var keyword:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wordNav = findViewById(R.id.wordNav)
        listView = findViewById(R.id.listView)
        searchView = findViewById(R.id.search_view)
        toolbar = findViewById(R.id.toolBar)
        tvBig = findViewById(R.id.tv_big)
        root = findViewById(R.id.root)
        setSupportActionBar(toolbar)
        wordNav.setListener { word ->
            updateList(word)
            updateList(word)
        }
        listView.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, FriendActivity::class.java)
            intent.putExtra("friend", list[position])
            startActivity(intent)
        }
        initSearch()
    }

    fun initSearch() {
        //搜索框文字变化监听
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                keyword = newText
                query()
                return false
            }

        })
    }

    override fun onResume() {
        super.onResume()
        query()
        root.requestFocus()
    }

    private fun updateList(word: String?) {
        for (index in list.indices) {
            val person = list[index]
            val header = person.header
            if (word == header) {
                listView.setSelection(index)
            }
        }
        if (word == null) {
            tvBig.visibility = View.GONE
            tvBig.text = ""
        } else {
            tvBig.visibility = View.VISIBLE
            tvBig.text = word
        }

    }


    fun query() {
        val database = AppDataBase.getDatabase(this)
        val giftDao = database?.giftDao()
        var key = "%${this.keyword}%"
        val single = giftDao?.getList(key)
        single?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<List<FriendEntity>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onSuccess(t: List<FriendEntity>) {
                    list.clear()
                    t.forEach {
                        list.add(Person(it))
                    }
                    list.sortWith(Comparator { o1, o2 -> o1.pinyin.compareTo(o2.pinyin) })
                    val adapter = MyAdapter(this@MainActivity, list)

                    listView.adapter = adapter
                    if (t.isNotEmpty()) {
                        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
                            override fun onScroll(
                                view: AbsListView?,
                                firstVisibleItem: Int,
                                visibleItemCount: Int,
                                totalItemCount: Int
                            ) {
                                try {
                                    wordNav.setTouchIndex(list[firstVisibleItem].header)
                                } catch (e: Exception) {

                                }

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
                val intent = Intent(this, AddFriendActivity::class.java)
                startActivity(intent)
            }
            R.id.statistics ->{
                val intent = Intent(this, StatisticsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}
