package com.lkw.learn.base

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.lkw.learn.R
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer


open class BaseActivity : AppCompatActivity() {
    private var exitTime :Long= -1

    override fun onBackPressed() {
        val now = SystemClock.elapsedRealtime()
        if (exitTime < 0 || now - exitTime > 1500) {
            exitTime = now
            toast(R.string.press_again_finish)
        } else {
            super.onBackPressed()
        }
    }

    protected fun toast(string: String){
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
    }

    protected fun toast(@StringRes resId: Int){
        Toast.makeText(this,resId,Toast.LENGTH_SHORT).show()
    }


}