package org.aaron.widgetlibrary.activity

import android.app.Activity
import android.os.Bundle
import org.aaron.widgetlibrary.databinding.ActivityGlideBinding
import org.aaron.widgetlibrary.utils.Constants
import org.aaron.widgetlibrary.utils.LoadImageUtils

/**
 * Created by wangxl1 on 2022/11/15 09:35
 * E-Mail Address： wang_x_le@163.com
 */
class GlideActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGlideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LoadImageUtils.loadImageView(binding.myImageView, Constants.url_android)
        LoadImageUtils.loadImageView(binding.myImageView2, Constants.url_gacha)
    }
}
