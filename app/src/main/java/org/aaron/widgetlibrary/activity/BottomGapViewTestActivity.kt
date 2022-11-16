package org.aaron.widgetlibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.aaron.widgetlibrary.R

/**
 * Created by wangxl1 on 2022/10/9 11:18
 * E-Mail Address： wang_x_le@163.com
 */
class BottomGapViewTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "自定义View"
        setContentView(R.layout.activity_bottom_gap_view_text)
    }
}