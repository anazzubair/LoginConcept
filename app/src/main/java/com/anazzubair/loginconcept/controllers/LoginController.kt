package com.anazzubair.loginconcept.controllers

import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import com.anazzubair.loginconcept.R
import com.anazzubair.loginconcept.transitions.Rotate
import com.anazzubair.loginconcept.transitions.TextResize
import com.transitionseverywhere.ChangeBounds
import com.transitionseverywhere.Transition
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet

class LoginController : BaseAuthController() {

    override fun onAttach(view: View) {
        super.onAttach(view)

        caption.setText(R.string.log_in_label)
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.color_log_in))

        views = listOf(view.findViewById(R.id.email_input_edit), view.findViewById(R.id.password_input_edit))
        views.forEach { editText ->
            if(editText.id == R.id.password_input_edit) {
                val inputLayout = view.findViewById<TextInputLayout>(R.id.password_input)
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                inputLayout.setTypeface(boldTypeface)

                editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        inputLayout.isPasswordVisibilityToggleEnabled = (editText.length() > 0)
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
                })

                editText.setOnFocusChangeListener { _, hasFocus ->
                    if(hasFocus) return@setOnFocusChangeListener
                    editText.isSelected = (editText.text.isNotEmpty())
                }
            }
        }
    }

    override fun getAuthLayoutId(): Int = R.layout.login_view

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun fold() {
        lock = true
        val padding = getTextPadding()

        val rotateTransition = Rotate()
        rotateTransition.endAngle = -90f
        rotateTransition.addTarget(caption)

        val transitionSet = TransitionSet().apply {
            duration = resources?.getInteger(R.integer.duration)!!.toLong()
            addTransition(ChangeBounds())
            addTransition(rotateTransition)
            addTransition(TextResize().addTarget(caption))
            ordering = TransitionSet.ORDERING_TOGETHER
            addListener(object : Transition.TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition?) {
                    super.onTransitionEnd(transition)
                    caption.translationX = -1 * padding
                    caption.rotation = 0f
                    caption.isVerticalText = true
                    caption.requestLayout()
                    lock = false
                }
            })
        }
        TransitionManager.beginDelayedTransition(parent, transitionSet)
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, caption.textSize / 2)
        caption.setTextColor(Color.WHITE)
        val params = ConstraintLayout.LayoutParams::class.java.cast(caption.layoutParams)
        params.leftToLeft = ConstraintLayout.LayoutParams.UNSET
        params.verticalBias = 0.5f
        caption.layoutParams = params
        caption.translationX = caption.width / 8 - padding

    }

    private fun getTextPadding(): Float {
        return resources?.let {
            it.getDimension(R.dimen.folded_label_padding) / 2f
        } ?: 0f
    }
}
