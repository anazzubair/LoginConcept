package com.anazzubair.loginconcept.customviews

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.Log
import com.anazzubair.loginconcept.R

class VerticalTextView : AppCompatTextView {

    var isVerticalText = false
    var isTopDown = false

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(context, attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initAttributes(context, attrs)
    }

    private fun initAttributes(context: Context?, attrs: AttributeSet?) {
        context?.let {
            attrs?.let {
                val array = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView)
                isVerticalText = array.getBoolean(R.styleable.VerticalTextView_is_vertical_text, false)
                isTopDown = array.getBoolean(R.styleable.VerticalTextView_is_top_down, false)
                array.recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if(!isVerticalText) return

        val height = measuredWidth
        val width = measuredHeight
        setMeasuredDimension(width, height)
        Log.d("VerticalTextView", "VerticalTextView Width: $width Height: $height")
    }

    override fun onDraw(canvas: Canvas) {
        if(!isVerticalText) {
            return super.onDraw(canvas)
        }

        val textPaint = paint.apply {
            color = currentTextColor
            drawableState = getDrawableState()
        }

        canvas.save()

        if (isTopDown) {
            canvas.translate(width.toFloat(), 0f)
            canvas.rotate(90f)
        } else {
            canvas.translate(0f, height.toFloat())
            canvas.rotate(-90f)
        }

        canvas.translate(compoundPaddingLeft.toFloat(), extendedPaddingTop.toFloat())

        layout.draw(canvas)
        canvas.restore()
    }

    private fun text() { this.text.toString() }
}