package org.aaron.widgetlibrary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.aaron.widgetlibrary.activity.BottomGapViewTestActivity
import org.aaron.widgetlibrary.activity.CardViewTestActivity
import org.aaron.widgetlibrary.activity.ExpandTextTestActivity
import org.aaron.widgetlibrary.activity.FrescoActivity
import org.aaron.widgetlibrary.activity.GlideActivity
import org.aaron.widgetlibrary.activity.LoopScalingTestActivity
import org.aaron.widgetlibrary.activity.ShadowViewTestActivity
import org.aaron.widgetlibrary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvExpandText.setOnClickListener {
            startActivity(Intent(this, ExpandTextTestActivity::class.java))
        }

        binding.tvGapView.setOnClickListener {
            startActivity(Intent(this, BottomGapViewTestActivity::class.java))
        }

        binding.tvCardView.setOnClickListener {
            startActivity(Intent(this, CardViewTestActivity::class.java))
        }

        binding.tvShadowView.setOnClickListener {
            startActivity(Intent(this, ShadowViewTestActivity::class.java))
        }

        binding.tvFrescoView.setOnClickListener {
            startActivity(Intent(this, FrescoActivity::class.java))
        }

        binding.tvGlideView.setOnClickListener {
            startActivity(Intent(this, GlideActivity::class.java))
        }

        binding.btnLoopScaling.setOnClickListener {
            startActivity(Intent(this, LoopScalingTestActivity::class.java))
        }
    }
}
