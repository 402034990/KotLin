package com.example.administrator.kotlin

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.kotlin_demo.R


/**
 * Created by Administrator on 2017/5/5 0005.
 */

class FlowIndicator : View {
    private var mCount: Int = 0
    private var mFocus: Int = 0
    private var mNormal_Color: Int = 0
    private var mFocus_Color: Int = 0
    private var mSpace: Int = 0
    private var mRadius: Int = 0
    internal var mPaint: Paint? = null

    constructor(context: Context) : super(context) {}

    fun getmCount(): Int {
        return mCount
    }

    fun setmCount(mCount: Int) {
        this.mCount = mCount
        invalidate()
    }

    fun getmFocus(): Int {
        return mFocus
    }

    fun setmFocus(mFocus: Int) {
        this.mFocus = mFocus
        invalidate()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.FlowIndicator)
        mCount = array.getInt(R.styleable.FlowIndicator_count, 1)
        mFocus = array.getInt(R.styleable.FlowIndicator_focus, 0)
        mSpace = array.getDimensionPixelOffset(R.styleable.FlowIndicator_space, 10)
        mRadius = array.getDimensionPixelOffset(R.styleable.FlowIndicator_r, 10)
        mNormal_Color = array.getColor(R.styleable.FlowIndicator_normal_color, Color.WHITE)
        mFocus_Color = array.getColor(R.styleable.FlowIndicator_focus_color, Color.RED)

        mPaint = Paint()
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getMeasuredWidth(widthMeasureSpec), getMeasuredHeight(heightMeasureSpec))
    }

    private fun getMeasuredHeight(heightMeasureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(heightMeasureSpec)
        var size = View.MeasureSpec.getSize(heightMeasureSpec)
        var result = size
        if (mode != View.MeasureSpec.EXACTLY) {
            size = paddingBottom + paddingTop + mRadius * 2
            result = Math.min(result, size)
        }
        return result
    }

    private fun getMeasuredWidth(widthMeasureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(widthMeasureSpec)
        var size = View.MeasureSpec.getSize(widthMeasureSpec)
        var result = size
        if (mode != View.MeasureSpec.EXACTLY) {
            size = paddingLeft + paddingRight + mCount * 2 * mRadius + mSpace * (mCount - 1)
            result = Math.min(result, size)
        }
        return result
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint!!.isAntiAlias = true
        if (mCount == 0) {
            return
        }

        val left = width - (mCount * 2 * mRadius + mSpace * (mCount - 1)) / 2

        for (i in 0..mCount - 1) {
            val x = leftPaddingOffset + i * (2 * mRadius + mSpace) + mRadius
            val color = if (i == mFocus) mFocus_Color else mNormal_Color
            mPaint!!.color = color
            canvas.drawCircle(x.toFloat(), (height / 2).toFloat(), mRadius.toFloat(), mPaint)
        }
    }
}
