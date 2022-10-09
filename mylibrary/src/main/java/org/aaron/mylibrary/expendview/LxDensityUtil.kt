package org.aaron.mylibrary.expendview

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import java.lang.reflect.Field

/**
 * @author Ivan
 */
object LxDensityUtil {
    fun dip2px(c: Context, dpValue: Float): Int {
        val scale = c.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun dip2sp(c: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            c.resources.displayMetrics
        )
            .toInt()
    }

    fun px2dip(c: Context, pxValue: Float): Int {
        val scale = c.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun px2sp(c: Context, pxValue: Float): Int {
        val fontScale = c.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun sp2px(c: Context, spValue: Float): Int {
        val fontScale = c.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun sp2dip(c: Context, spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            c.resources.displayMetrics
        )
            .toInt()
    }

    fun getScreenW(c: Context): Int {
        return c.resources.displayMetrics.widthPixels
    }

    fun getScreenH(c: Context): Int {
        return c.resources.displayMetrics.heightPixels
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getScreenRealH(context: Context): Int {
        val h: Int
        val winMgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = winMgr.defaultDisplay
        val dm = DisplayMetrics()
        h = if (Build.VERSION.SDK_INT >= 17) {
            display.getRealMetrics(dm)
            dm.heightPixels
        } else {
            try {
                val method = Class.forName("android.view.Display").getMethod(
                    "getRealMetrics",
                    DisplayMetrics::class.java
                )
                method.invoke(display, dm)
                dm.heightPixels
            } catch (e: Exception) {
                display.getMetrics(dm)
                dm.heightPixels
            }
        }
        return h
    }

    fun getStatusBarH(context: Context): Int {
        val c: Class<*>
        val obj: Any
        val field: Field
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            val x = field[obj].toString().toInt()
            statusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return statusBarHeight
    }

    fun getNavigationBarrH(c: Context): Int {
        val resources = c.resources
        val identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelOffset(identifier)
    }
}