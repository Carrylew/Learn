package com.lkw.learn.components

import android.os.Bundle
import android.util.Log
import com.lkw.learn.R
import com.lkw.learn.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("lkw","")
    }
}
