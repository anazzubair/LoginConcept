package com.anazzubair.loginconcept.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.SparseArray
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import collections.forEach
import com.anazzubair.loginconcept.R
import com.anazzubair.loginconcept.controllers.BaseAuthController
import com.anazzubair.loginconcept.controllers.LoginController
import com.anazzubair.loginconcept.customviews.AnimatedViewPager
import com.anazzubair.loginconcept.controllers.SignUpController
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.support.RouterPagerAdapter

class AuthPagerAdapter(private val host: Controller, private val viewPager: AnimatedViewPager,
                       private val sharedElements: List<ImageView>, private val background: ImageView)
    : RouterPagerAdapter(host), BaseAuthController.Callback {

    private val pages: SparseArray<BaseAuthController>

    init {
        pages = SparseArray(count)
        pages.put(0, LoginController().apply { callback = this@AuthPagerAdapter })
        pages.put(1, SignUpController().apply { callback = this@AuthPagerAdapter })
        viewPager.duration = 300
    }

    override fun configureRouter(router: Router, position: Int) {
        if (router.hasRootController()) return
        when (position) {
            0 -> router.setRoot(RouterTransaction.with(pages[0]))
            1 -> router.setRoot(RouterTransaction.with(pages[1]))
            else -> throw IllegalStateException()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageWidth(position: Int): Float {
        val textSize = host.resources!!.getDimension(R
                .dimen.folded_size)
        val textPadding = host.resources!!.getDimension(R.dimen.folded_label_padding)
        return (1 - (textSize + textPadding) / viewPager.width)
    }

    override fun show(nextController: BaseAuthController) {
        val controllerIndex = pages.keyAt(pages.indexOfValue(nextController))
        viewPager.setCurrentItem(controllerIndex, true)
        val pageWidth = host.view!!.width
        shiftSharedElements(pageWidth - pageWidth * getPageWidth(controllerIndex), controllerIndex == 1)
        pages.forEach { index, controller -> if(index != controllerIndex) controller.fold() }
    }

    private fun shiftSharedElements(pageOffsetX: Float, forward: Boolean) {
        val context = viewPager.context
        //since we're clipping the page, we have to adjust the shared elements
        val shiftAnimator = AnimatorSet()
        for (view in sharedElements) {
            var translationX = if (forward) pageOffsetX else -pageOffsetX
            val temp = view.width / 3f
            translationX -= if (forward) temp else -temp
            val shift = ObjectAnimator.ofFloat<View>(view, View.TRANSLATION_X, 0f, translationX)
            shiftAnimator.playTogether(shift)
        }

        val color = ContextCompat.getColor(context, if (forward) R.color.color_logo_sign_up else R.color.color_logo_log_in)
        DrawableCompat.setTint(sharedElements.get(0).getDrawable(), color)
        //scroll the background by x
        val offset = background.width / 2
        val scrollAnimator = ObjectAnimator.ofInt(background, "scrollX", if (forward) offset else -offset)
        shiftAnimator.playTogether(scrollAnimator)
        shiftAnimator.interpolator = AccelerateDecelerateInterpolator()
        shiftAnimator.duration = viewPager.resources.getInteger(R.integer.duration) / 2L
        shiftAnimator.start()
    }

    override fun scale(hasFocus: Boolean) {
        val scale = if (hasFocus) 1f else 1.4f
        val logoScale = if (hasFocus) 0.75f else 1f
        val logo = sharedElements[0]

        val scaleAnimation = AnimatorSet()
        scaleAnimation.playTogether(ObjectAnimator.ofFloat(logo, View.SCALE_X, logoScale))
        scaleAnimation.playTogether(ObjectAnimator.ofFloat(logo, View.SCALE_Y, logoScale))
        scaleAnimation.playTogether(ObjectAnimator.ofFloat<View>(background, View.SCALE_X, scale))
        scaleAnimation.playTogether(ObjectAnimator.ofFloat<View>(background, View.SCALE_Y, scale))
        scaleAnimation.duration = 200
        scaleAnimation.interpolator = AccelerateDecelerateInterpolator()
        scaleAnimation.start()
    }
}