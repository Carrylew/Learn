package com.lkw.learn.components

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lkw.learn.R
import com.lkw.learn.base.BaseActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer

class WelcomeActivity : BaseActivity() {
    var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission(permissions, Consumer { aBoolean ->
            if (!aBoolean) {
                finish()
            } else {
                navTo()
            }
        })
    }

    private fun navTo() {
        setContentView(R.layout.activity_welcome)
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun requestPermission(permissions: Array<String>, observer: Consumer<Boolean>) {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(*permissions).subscribe(observer)
    }
}
