package com.anazzubair.loginconcept.controllers

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Point
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.anazzubair.loginconcept.R
import com.anazzubair.loginconcept.adapters.AuthPagerAdapter
import com.anazzubair.loginconcept.customviews.AnimatedViewPager
import com.anazzubair.loginconcept.glide.ControllerRequestManager
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget


class PagerController : Controller() {

    private lateinit var pagerAdapter: RouterPagerAdapter
    private lateinit var sharedElements: List<ImageView>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view =  inflater.inflate(R.layout.pager_view, container, false)
        val logo = view.findViewById<ImageView>(R.id.logo)
        val background = view.findViewById<ImageView>(R.id.scrolling_background)
        val pager = view.findViewById<AnimatedViewPager>(R.id.pager)
        val facebook = view.findViewById<ImageView>(R.id.first)
        val linkedIn = view.findViewById<ImageView>(R.id.second)
        val twitter = view.findViewById<ImageView>(R.id.last)
        sharedElements  = listOf(logo, facebook, linkedIn, twitter)


        @ColorRes val color = R.color.color_logo_log_in
        DrawableCompat.setTint(logo.drawable, ContextCompat.getColor(container.context, color))

        val screenSize = screenSize()

        ControllerRequestManager.with(this)
                .asBitmap()
                .apply(RequestOptions()
                        .override(screenSize[0] * 2, screenSize[1])
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(R.drawable.busy)
                .into(object : ImageViewTarget<Bitmap>(background){
                    override fun setResource(resource: Bitmap?) {
                        background.setImageBitmap(resource)
                        background.post{
                            background.scrollTo(-background.width/2, 0)
                            Log.d("ImageSize", background.width.toString())
                            val xAnimator = ObjectAnimator.ofFloat(background, View.SCALE_X,4f, 1.4f)
                            val yAnimator = ObjectAnimator.ofFloat(background, View.SCALE_Y, 4f, 1.4f)
                            val animatorSet = AnimatorSet()
                            animatorSet.playTogether(xAnimator, yAnimator)
                            animatorSet.duration = 300
                            animatorSet.start()
                        }
                        pager.post {
                            pager.adapter = AuthPagerAdapter(this@PagerController, pager, sharedElements, background)
                        }
                    }
                })

        return view
    }

    private fun screenSize(): IntArray {
        if(activity == null) throw IllegalStateException("Controller not bound to a context. Cannot get screen size")
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return intArrayOf(size.x, size.y)
    }
}
