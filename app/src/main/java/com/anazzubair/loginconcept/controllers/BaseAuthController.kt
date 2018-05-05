package com.anazzubair.loginconcept.controllers

import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anazzubair.loginconcept.R
import com.anazzubair.loginconcept.customviews.VerticalTextView
import com.anazzubair.loginconcept.transitions.Rotate
import com.anazzubair.loginconcept.transitions.TextResize
import com.bluelinelabs.conductor.Controller
import com.transitionseverywhere.ChangeBounds
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

abstract class BaseAuthController : Controller() {

    protected var lock: Boolean = true
    protected lateinit var parent: ViewGroup
    protected lateinit var caption: VerticalTextView
    protected lateinit var views: List<TextInputEditText>
    protected lateinit var viewPager: ViewPager
    lateinit var callback: Callback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val root = inflater.inflate(getAuthLayoutId(), container, false)
        viewPager = container as ViewPager
        caption = root.findViewById(R.id.caption)
        parent = root.findViewById(R.id.root)
        KeyboardVisibilityEvent.setEventListener(activity!!) { isOpen ->
            callback.scale(isOpen)
            if (!isOpen) {
                clearFocus()
            }
        }
        root.setOnClickListener { unfold() }
        return root
    }

    @LayoutRes
    abstract fun getAuthLayoutId(): Int
    abstract fun fold()

    private fun unfold() {
        if(lock) return

        caption.isVerticalText = false
        caption.requestLayout()

        val rotateTransition = Rotate().apply {
            startAngle = -90f
            endAngle = 0f
            addTarget(caption)
        }

        val transitionSet = TransitionSet().apply {
            duration = resources?.getInteger(R.integer.duration)!!.toLong()
            addTransition(ChangeBounds())
            addTransition(rotateTransition)
            addTransition(TextResize().addTarget(caption))
            ordering = TransitionSet.ORDERING_TOGETHER
        }
        caption.post {
            TransitionManager.beginDelayedTransition(parent, transitionSet)
            caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources!!.getDimension(R.dimen.unfolded_size))
            caption.setTextColor(ContextCompat.getColor(parent.context, R.color.color_label))
            caption.translationX = 0f
            val params = ConstraintLayout.LayoutParams::class.java.cast(caption.layoutParams)
            params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
            params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            params.verticalBias = 0.78f
            caption.layoutParams = params
        }
        callback.show(this)
        lock = true
    }

    private fun clearFocus() = views.forEach { it.clearFocus() }

    interface Callback {
        fun show(nextController: BaseAuthController)
        fun scale(hasFocus: Boolean)
    }
}