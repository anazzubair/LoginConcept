package com.anazzubair.loginconcept.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.Scroller
import com.anazzubair.loginconcept.adapters.AuthPagerAdapter
import com.anazzubair.loginconcept.controllers.BaseAuthController

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

        this.addOnPageChangeListener(object: OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) =
                (this@AnimatedViewPager.adapter as AuthPagerAdapter).showSelectedController(p0)

        })

    }

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