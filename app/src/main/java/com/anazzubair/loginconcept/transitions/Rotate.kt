package com.anazzubair.loginconcept.transitions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.anazzubair.loginconcept.R
import androidx.transition.Transition
import androidx.transition.TransitionValues

class Rotate : Transition
{
    var startAngle: Float = 0f
    var endAngle: Float = 0f

    companion object {
        const val PROPNAME_ROTATION = "anazzubair:rotate:rotation"
    }

    constructor()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        context?.let {
            attrs?.let {
                val array = context.obtainStyledAttributes(attrs, R.styleable.Rotate)
                startAngle = array.getFloat(R.styleable.Rotate_start_angle, 0f)
                endAngle = array.getFloat(R.styleable.Rotate_end_angle, 0f)
                array.recycle()
            }
        }
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        transitionValues.values[PROPNAME_ROTATION] = startAngle
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        transitionValues.values[PROPNAME_ROTATION] = endAngle
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?,
                                endValues: TransitionValues?): Animator? {
        if (startValues == null || endValues == null) {
            return null
        }
        val view = endValues.view
        val startRotation = startValues.values[PROPNAME_ROTATION] as Float
        val endRotation = endValues.values[PROPNAME_ROTATION] as Float
        if (startRotation != endRotation) {
            view.rotation = startRotation
            return ObjectAnimator.ofFloat(view, View.ROTATION,
                    startRotation, endRotation)
        }
        return null
    }
}