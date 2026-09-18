package org.aaron.widgetlibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import org.aaron.mylibrary.loopview.LoopScalingLayoutManager
import org.aaron.widgetlibrary.R

/**
 * 测试页面：展示 LoopScalingLayoutManager 的高级交互效果。
 * 包含：
 * 1. 复杂布局适配：每个 Item 均使用 CardView + ImageView + TextView 的组合。
 * 2. 磁吸对齐 (LoopSnapHelper)：确保用户滑动停止后，Item 始终对齐到放大的起始状态。
 * 3. 点击自动居中：点击右侧任何小 Item，列表会自动平滑滚动将其置顶并放大。
 * 4. 大数据集模拟：通过虚拟的 getItemCount 配合取模运算，实现真正意义上的无限循环滑动。
 */
class LoopScalingTestActivity : AppCompatActivity() {

    data class ItemModel(val imageRes: Int, val title: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_scaling_test)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_loop)
        
        // Ensure no clipping for the scaling shadows
        recyclerView.clipToPadding = false
        recyclerView.clipChildren = false

        val items = listOf(
            ItemModel(R.mipmap.ic_launcher, "Android 1"),
            ItemModel(R.mipmap.ic_launcher_round, "Kotlin 2"),
            ItemModel(R.mipmap.ic_launcher, "Compose 3"),
            ItemModel(R.mipmap.ic_launcher_round, "Gradle 4"),
            ItemModel(R.mipmap.ic_launcher, "Layout 5"),
            ItemModel(R.mipmap.ic_launcher_round, "View 6")
        )

        val lm = LoopScalingLayoutManager(resources.displayMetrics.density)
        // 示例：动态控制惯性属性
        lm.isFlingEnabled = true
        lm.flingWeight = 1.2f 
        
        recyclerView.layoutManager = lm
        recyclerView.adapter = LoopAdapter(items) { position ->
            // Click to auto-center/expand
            recyclerView.smoothScrollToPosition(position)
        }

        val snapHelper = LoopSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
    }

    class LoopSnapHelper : SnapHelper() {
        override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray {
            val out = IntArray(2)
            if (layoutManager is LoopScalingLayoutManager) {
                val unit = layoutManager.UNIT
                val offset = layoutManager.horizontalOffset
                // 直接根据偏移量计算，避免 View 坐标带来的像素级误差
                val rem = offset % unit
                if (rem == 0) return out
                
                out[0] = if (rem < unit / 2) -rem else unit - rem
            }
            return out
        }

        override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
            return if (layoutManager is LoopScalingLayoutManager) {
                layoutManager.findPinnedView()
            } else null
        }

        override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, vX: Int, vY: Int): Int {
            if (layoutManager !is LoopScalingLayoutManager) return RecyclerView.NO_POSITION
            
            // 如果关闭惯性滑动，返回当前位置，SnapHelper 会自动吸附到最近的一个
            if (!layoutManager.isFlingEnabled) return RecyclerView.NO_POSITION

            val itemCount = layoutManager.itemCount
            if (itemCount == 0) return RecyclerView.NO_POSITION

            val unit = layoutManager.UNIT
            val currentPos = layoutManager.horizontalOffset / unit
            
            // 使用 flingWeight 控制惯性距离
            // 计算逻辑：速度 / (基准阈值 * 权重)
            val baseThreshold = 2500f
            val delta = (vX / (baseThreshold / layoutManager.flingWeight)).toInt().coerceIn(-5, 5)
            
            return (currentPos + delta).coerceAtLeast(0)
        }
    }

    class LoopAdapter(
        private val items: List<ItemModel>,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.Adapter<LoopAdapter.VH>() {
        
        class VH(view: View) : RecyclerView.ViewHolder(view) {
            val iv: ImageView = view.findViewById(R.id.iv_image)
            val tv: TextView = view.findViewById(R.id.tv_text)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_loop_complex, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position % items.size]
            holder.iv.setImageResource(item.imageRes)
            holder.tv.text = item.title
            holder.itemView.setOnClickListener { onItemClick(position) }
        }

        override fun getItemCount(): Int = 100000
    }
}
