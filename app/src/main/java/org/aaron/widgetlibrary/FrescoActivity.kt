package org.aaron.widgetlibrary

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_fresco.*


/**
 * Created by wangxl1 on 2022/11/14 17:59
 * E-Mail Address： wang_x_le@163.com
 */
class FrescoActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fresco)

        val uri: Uri =
            Uri.parse("https://upload-images.jianshu.io/upload_images/5530180-cf7f0e7d16cd55ed.gif?imageMogr2/auto-orient/strip|imageView2/2/w/800/format/webp")

        my_image_view.setImageURI(uri, null)



        val uri2 =
            Uri.parse("https://pic.wangxiangle.top/img/477991a34fe00025aa0bbfe0190a420b.webp")
        val controller = Fresco.newDraweeControllerBuilder()
            .setUri(uri2)
            .setAutoPlayAnimations(true)
            .build()
        my_image_view2.setController(controller)
//        my_image_view2.setImageURI(uri2)


    }
}