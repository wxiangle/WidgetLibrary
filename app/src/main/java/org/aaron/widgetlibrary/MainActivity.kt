package org.aaron.widgetlibrary

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_expand_text?.setOnClickListener {
            startActivity(Intent(this, ExpandTextTestActivity::class.java))
        }

        tv_gap_view?.setOnClickListener {
            startActivity(Intent(this, BottomGapViewTestActivity::class.java))
        }

        tv_card_view?.setOnClickListener {
            startActivity(Intent(this, CardViewTestActivity::class.java))
        }

        tv_shadow_view?.setOnClickListener {
            startActivity(Intent(this,ShadowViewTestActivity::class.java))
        }
    }
}