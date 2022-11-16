package org.aaron.widgetlibrary.activity

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by wangxl1 on 2022/11/14 18:09
 * E-Mail Address： wang_x_le@163.com
 */
class DemoApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}