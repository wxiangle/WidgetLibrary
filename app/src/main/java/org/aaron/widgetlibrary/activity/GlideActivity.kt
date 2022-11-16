package org.aaron.widgetlibrary.activity

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_glide.*
import org.aaron.widgetlibrary.R
import org.aaron.widgetlibrary.utils.Constants
import org.aaron.widgetlibrary.utils.LoadImageUtils

/**
 * Created by wangxl1 on 2022/11/15 09:35
 * E-Mail Address： wang_x_le@163.com
 */
class GlideActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)

        LoadImageUtils.loadImageView(my_image_view, Constants.url_android)
        LoadImageUtils.loadImageView(my_image_view2, Constants.url_gacha)

    }
}