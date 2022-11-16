package org.aaron.widgetlibrary.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_fresco.*
import org.aaron.widgetlibrary.R
import org.aaron.widgetlibrary.utils.Constants


/**
 * Created by wangxl1 on 2022/11/14 17:59
 * E-Mail Address： wang_x_le@163.com
 */
class FrescoActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fresco)

        val uri: Uri =
            Uri.parse(Constants.url_android)

        val controller = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setAutoPlayAnimations(true)
            .build()
        my_image_view.controller = controller
//        my_image_view.setImageURI(uri, null)



        val uri2 =
            Uri.parse(Constants.url_gacha)
        val controller2 = Fresco.newDraweeControllerBuilder()
            .setUri(uri2)
            .setAutoPlayAnimations(true)
            .build()
        my_image_view2.setController(controller2)
//        my_image_view2.setImageURI(uri2)


    }
}