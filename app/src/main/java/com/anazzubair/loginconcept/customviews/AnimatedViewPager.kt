package com.anazzubair.loginconcept.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.Scroller

class AnimatedViewPager : ViewPager {

    var duration: Int = 0

    constructor(context: Context) : super(context) {
        postInitViewPager()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        postInitViewPager()
    }

    private fun postInitViewPager() {
        try {
            val scroller = ViewPager::class.java.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val mScroller = ScrollerCustomDuration(context, DecelerateInterpolator())
            scroller.set(this, mScroller)
        } catch (e: Exception) {
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = false

    override fun onTouchEvent(ev: MotionEvent): Boolean = false

    inner class ScrollerCustomDuration : Scroller {

        constructor(context: Context) : super(context)

        constructor(context: Context, interpolator: Interpolator) : super(context, interpolator)

        @SuppressLint("NewApi")
        constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(context, interpolator, flywheel)

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, this@AnimatedViewPager.duration)
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
            super.startScroll(startX, startY, dx, dy, this@AnimatedViewPager.duration)
        }
    }
}