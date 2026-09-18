package org.aaron.mylibrary.loopview

import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * 高级自定义 LayoutManager，用于实现“首位固定缩放”的循环滚动效果。
 *
 * 核心特性与优化点：
 * 1. 中心锚点缩放 (Center Anchor Scaling)：所有 Item 的放大和缩小均以中心点为基准，避免左上角拉扯感。
 * 2. 联动位移算法：首位 Item 缩小的同时，次位 Item 缓缓滑入并放大，确保间距恒定为 9dp。
 * 3. 非线性动画 (Non-linear Animation)：内置 DecelerateInterpolator，使缩放手感更加灵动自然。
 * 4. 动态 Z 轴提升 (Z-Index Tracking)：根据进度动态调整 translationZ（最高达 12dp），实现完美的阴影堆叠效果。
 * 5. 边界保护：向左支持无限循环滚动，向右滑动到第一个元素时自动拦截停止。
 * 6. 精确渲染：采用 EXACTLY 测量模式，彻底解决图片放大时可能出现的白边或锯齿问题。
 * 7. 适配性：完美支持 RecyclerView 的 PaddingTop/Bottom 属性，确保 Item 始终垂直居中。
 */
class LoopScalingLayoutManager(private val density: Float) : RecyclerView.LayoutManager() {

    private fun dp(value: Float) = (value * density).toInt()
    private fun dpF(value: Float) = value * density

    val LARGE_WIDTH = dp(80f)
    val LARGE_HEIGHT = dp(100f)
    val SMALL_WIDTH = dp(60f)
    val SMALL_HEIGHT = dp(80f)
    val GAP = dp(9f)
    val UNIT = LARGE_WIDTH + GAP

    private val interpolator = DecelerateInterpolator(1.5f)
    var horizontalOffset = 0
        internal set

    /**
     * 是否启用惯性滑动（Fling）
     */
    var isFlingEnabled: Boolean = false

    /**
     * 惯性敏感度系数，值越大滑动越远，建议范围 [0.5, 2.0]
     */
    var flingWeight: Float = 1.0f

    // Pre-calculated centers
    private var center0 = 0f
    private var center1Start = 0f
    private var moveDistance1 = 0f
    private val moveDistanceOthers = (SMALL_WIDTH + GAP).toFloat()

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun canScrollHorizontally(): Boolean = true

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        // 优化：防止极小偏移导致的抖动
        if (dx == 0) return 0
        
        val delta = if (horizontalOffset + dx < 0) -horizontalOffset else dx
        if (delta == 0) return 0
        
        horizontalOffset += delta
        fill(recycler, state)
        return delta
    }

    /**
     * 获取当前处于固定位置（或最接近）的 View
     */
    fun findPinnedView(): View? {
        if (childCount == 0) return null
        val firstPos = horizontalOffset / UNIT
        // 查找 globalPos 等于 firstPos 的 View
        for (i in 0 until childCount) {
            val view = getChildAt(i) ?: continue
            val lp = view.layoutParams as RecyclerView.LayoutParams
            val viewPos = lp.viewAdapterPosition
            // 由于是循环列表，需要考虑取模后的匹配
            if (viewPos == (firstPos % itemCount + itemCount) % itemCount) {
                return view
            }
        }
        return getChildAt(0)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        
        // Update pre-calculated centers
        center0 = paddingLeft + LARGE_WIDTH / 2f
        center1Start = paddingLeft + LARGE_WIDTH + GAP + SMALL_WIDTH / 2f
        moveDistance1 = center1Start - center0

        detachAndScrapAttachedViews(recycler)
        fill(recycler, state)
    }

    private fun fill(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)
        val itemCount = state.itemCount
        if (itemCount == 0) return

        val firstVisiblePos = horizontalOffset / UNIT
        val baseProgress = abs(horizontalOffset % UNIT).toFloat() / UNIT
        val progress = interpolator.getInterpolation(baseProgress)

        val displayCount = ((width - paddingLeft - paddingRight) / (SMALL_WIDTH + GAP)) + 3

        // Draw order (bottom to top): Others -> Pinned (0) -> Sliding (1)
        for (i in displayCount.toInt() downTo 2) {
            layoutItem(recycler, firstVisiblePos + i, i, progress, itemCount)
        }
        layoutItem(recycler, firstVisiblePos, 0, progress, itemCount, isPinned = true)
        layoutItem(recycler, firstVisiblePos + 1, 1, progress, itemCount)
    }

    private fun layoutItem(
        recycler: RecyclerView.Recycler,
        globalPos: Int,
        relativeIdx: Int,
        progress: Float,
        itemCount: Int,
        isPinned: Boolean = false
    ) {
        val actualPos = (globalPos % itemCount + itemCount) % itemCount
        val view = recycler.getViewForPosition(actualPos)
        addView(view)

        val w: Int
        val h: Int
        val centerX: Float
        val centerY = paddingTop + (height - paddingTop - paddingBottom) / 2f

        when {
            isPinned -> {
                w = (LARGE_WIDTH - progress * (LARGE_WIDTH - SMALL_WIDTH)).toInt()
                h = (LARGE_HEIGHT - progress * (LARGE_HEIGHT - SMALL_HEIGHT)).toInt()
                centerX = center0
                view.translationZ = dpF(4f) * (1 - progress)
            }
            relativeIdx == 1 -> {
                w = (SMALL_WIDTH + progress * (LARGE_WIDTH - SMALL_WIDTH)).toInt()
                h = (SMALL_HEIGHT + progress * (LARGE_HEIGHT - SMALL_HEIGHT)).toInt()
                centerX = center1Start - progress * moveDistance1
                view.translationZ = dpF(8f) + progress * dpF(4f) // Always top
            }
            else -> {
                w = SMALL_WIDTH
                h = SMALL_HEIGHT
                centerX = (center1Start + (relativeIdx - 1) * moveDistanceOthers) - progress * moveDistanceOthers
                view.translationZ = 0f
            }
        }

        val left = (centerX - w / 2f).toInt()
        val top = (centerY - h / 2f).toInt()
        
        // Exact measure to prevent sub-pixel artifacts (white edges)
        val lp = view.layoutParams as RecyclerView.LayoutParams
        lp.width = w
        lp.height = h
        view.measure(
            View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY)
        )
        
        layoutDecorated(view, left, top, left + w, top + h)
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        val scroller = object : LinearSmoothScroller(recyclerView.context) {
            override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
                return (position * UNIT) - horizontalOffset
            }
            override fun calculateSpeedPerPixel(displayMetrics: android.util.DisplayMetrics): Float {
                return 100f / displayMetrics.densityDpi
            }
        }
        scroller.targetPosition = position
        startSmoothScroll(scroller)
    }
}
