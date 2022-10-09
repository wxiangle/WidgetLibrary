package org.aaron.mylibrary.expendview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import java.lang.ref.WeakReference

/**
 * Created by wangxl1 on 1/12/22 11:21
 * E-Mail Address： wang_x_le@163.com
 */
class OffsetImageSpan(val context: Context, val offset: Int, bitmap: Bitmap, verticalAlignment: Int) : ImageSpan(context, bitmap, verticalAlignment) {

    private var mDrawableRef: WeakReference<Drawable>? = null
    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        kotlin.runCatching {
            val b: Drawable = getCachedDrawable()
            canvas.save()

            var transY = bottom - b.bounds.bottom + offset
            if (mVerticalAlignment == DynamicDrawableSpan.ALIGN_BASELINE) {
                transY -= paint.fontMetricsInt.descent
            }

            canvas.translate(x, transY.toFloat())
            b.draw(canvas)
            canvas.restore()
        }.getOrElse {
            super.draw(canvas, text, start, end, x, top, y, bottom, paint)
        }
    }

    private fun getCachedDrawable(): Drawable {
        val wr = mDrawableRef
        var d: Drawable? = null
        if (wr != null) {
            d = wr.get()
        }
        if (d == null) {
            d = drawable
            mDrawableRef = WeakReference<Drawable>(d)
        }
        return d!!
    }
}