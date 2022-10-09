package org.aaron.mylibrary.gapview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import org.aaron.mylibrary.R

/**
 * Created by wangxl1 on 2022/10/9 11:08
 * E-Mail Address： wang_x_le@163.com
 */
class BottomGapView2(context: Context, attributeSet: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attributeSet) {

    var mGapRadius = 0
    var mPath = Path()
    var mLeftArcRectF = RectF()
    var mRightArcRectF = RectF()

    init {

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BottomGapView)
        mGapRadius = typedArray.getDimension(R.styleable.BottomGapView_gap_radius, 12f).toInt()
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {

        mPath.reset()
        val rectTop = height.toFloat() - mGapRadius / 2
        mPath.moveTo(0f, height.toFloat())
        mLeftArcRectF.left = 0f
        mLeftArcRectF.top = rectTop
        mLeftArcRectF.right = mGapRadius.toFloat()
        mLeftArcRectF.bottom = height.toFloat() + mGapRadius / 2
        mPath.arcTo(mLeftArcRectF, 180f, 90f)
        mPath.lineTo(width - mGapRadius.toFloat(), rectTop)

        mRightArcRectF.left = width.toFloat() - mGapRadius
        mRightArcRectF.top = rectTop
        mRightArcRectF.right = width.toFloat()
        mRightArcRectF.bottom = height.toFloat() + mGapRadius / 2
        mPath.arcTo(mRightArcRectF, -90f, 90f)
        mPath.lineTo(width.toFloat(), height.toFloat())
        mPath.close()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas?.clipOutPath(mPath)
        }else{
            canvas?.clipPath(mPath,Region.Op.DIFFERENCE)
        }
        super.onDraw(canvas)
    }
}