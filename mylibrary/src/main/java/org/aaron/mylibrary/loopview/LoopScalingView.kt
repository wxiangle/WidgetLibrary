package org.aaron.mylibrary.loopview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.floor

/**
 * Custom View for a looping horizontal scroll with scaling items.
 * Created per user requirements.
 */
class LoopScalingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val density = context.resources.displayMetrics.density
    private fun dp(value: Float) = value * density

    // Dimensions in PX
    private val LARGE_WIDTH = dp(80f)
    private val LARGE_HEIGHT = dp(100f)
    private val SMALL_WIDTH = dp(60f)
    private val SMALL_HEIGHT = dp(80f)
    private val GAP = dp(9f)

    // The distance the "next" item travels to reach the pinned position.
    private val SCROLL_UNIT = LARGE_WIDTH + GAP

    private var images = mutableListOf<Drawable>()
    private var scrollOffset = 0f

    private val rectF = RectF()

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            scrollOffset += distanceX
            invalidate()
            return true
        }

        override fun onDown(e: MotionEvent): Boolean = true
    })

    /**
     * Set the list of image resource IDs to display.
     */
    fun setImages(resIds: List<Int>) {
        images.clear()
        resIds.forEach { id ->
            ContextCompat.getDrawable(context, id)?.let { images.add(it) }
        }
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        if (images.isEmpty()) return

        // Calculate current base index and progress [0, 1)
        val rawIndex = floor(scrollOffset / SCROLL_UNIT).toInt()
        val progress = (scrollOffset / SCROLL_UNIT) - rawIndex

        // Determine how many items to draw to fill the screen
        // We draw from right to left to ensure the leftmost items (which can overlap) 
        // are drawn in the correct order if needed.
        // Actually, Item 1 moves TO Item 0's position. Item 1 should be on top.
        
        val visibleCount = (width / (SMALL_WIDTH + GAP)).toInt() + 3
        
        // Draw order: 0, then 1, then 2... 
        // Item 1 covers Item 0 as it slides in.
        
        // Items to the right (2, 3, ...)
        for (i in visibleCount downTo 2) {
            drawItem(canvas, rawIndex + i, i, progress.toFloat())
        }

        // Item 0 (Pinned, shrinking)
        drawItem(canvas, rawIndex, 0, progress.toFloat(), isPinned = true)

        // Item 1 (Moving to 0, growing, on top of 0)
        drawItem(canvas, rawIndex + 1, 1, progress.toFloat())
    }

    private fun drawItem(canvas: Canvas, globalIdx: Int, relativeIdx: Int, progress: Float, isPinned: Boolean = false) {
        val count = images.size
        val imageIdx = ((globalIdx % count) + count) % count
        val drawable = images[imageIdx]

        val w: Float
        val h: Float
        val x: Float

        when {
            isPinned -> {
                // Item 0: pinned at paddingStart, shrinking from Large to Small
                w = LARGE_WIDTH - progress * (LARGE_WIDTH - SMALL_WIDTH)
                h = LARGE_HEIGHT - progress * (LARGE_HEIGHT - SMALL_HEIGHT)
                x = paddingLeft.toFloat()
            }
            relativeIdx == 1 -> {
                // Item 1: moving from (Large + Gap) to 0, growing from Small to Large
                w = SMALL_WIDTH + progress * (LARGE_WIDTH - SMALL_WIDTH)
                h = SMALL_HEIGHT + progress * (LARGE_HEIGHT - SMALL_HEIGHT)
                x = paddingLeft.toFloat() + (LARGE_WIDTH + GAP) * (1 - progress)
            }
            else -> {
                // Other items: constant small size, moving left by (Small + Gap)
                w = SMALL_WIDTH
                h = SMALL_HEIGHT
                // At p=0, Item 2 is at (L + G) + (S + G). At p=1, it is at (L + G).
                x = paddingLeft.toFloat() + (LARGE_WIDTH + GAP) + (relativeIdx - 1 - progress) * (SMALL_WIDTH + GAP)
            }
        }

        // Center vertically in view
        val y = (height - h) / 2
        
        rectF.set(x, y, x + w, y + h)
        drawable.bounds = android.graphics.Rect(
            rectF.left.toInt(), 
            rectF.top.toInt(), 
            rectF.right.toInt(), 
            rectF.bottom.toInt()
        )
        drawable.draw(canvas)
    }
}
