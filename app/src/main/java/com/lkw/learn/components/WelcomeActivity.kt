package com.lkw.learn.components

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lkw.learn.R
import com.lkw.learn.base.BaseActivity
import com.lkw.learn.services.PathService
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer
import java.io.*
import java.nio.file.Path
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.view.Window
import android.view.WindowManager


class WelcomeActivity : BaseActivity() {
    var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)
        requestPermission(permissions, Consumer { aBoolean ->
            if (!aBoolean) {
                finish()
            } else {
                Handler().postDelayed({
                    navTo()
                }, 1500)    //延时1.5s执行
            }
        })
    }

    private fun navTo() {
        if (copySqlite()) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }else{
            Toast.makeText(this,"无法关联数据",Toast.LENGTH_LONG).show()
        }
        finish()

    }

    /**
     * 复制数据库文件
     * return 是否继续
     */
    private fun copySqlite(): Boolean {

        val dbFile = PathService.dbFile
        if (dbFile.exists()) {
            return true
        }

        val assetManager = this.resources.assets
        val dbInputStream: InputStream?
        val dbOutputStream: OutputStream = FileOutputStream(dbFile)
        return try {
            dbInputStream = assetManager.open(PathService.FILE_DB)
            dbInputStream.copyTo(dbOutputStream)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun requestPermission(permissions: Array<String>, observer: Consumer<Boolean>) {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(*permissions).subscribe(observer)
    }
}
