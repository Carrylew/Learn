package com.lkw.learn.views

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView

class EChartsView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    WebView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)



    init {
        val webSettings = settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportZoom(false)
        webSettings.displayZoomControls = false
//        loadUrl("file:///android_asset/lineCharts.html")
    }
}