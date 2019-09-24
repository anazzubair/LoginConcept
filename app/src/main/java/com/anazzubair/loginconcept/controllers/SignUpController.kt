package com.anazzubair.loginconcept.controllers

import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.*
import com.anazzubair.loginconcept.R
import com.anazzubair.loginconcept.transitions.Rotate
import com.anazzubair.loginconcept.transitions.TextResize
import com.google.android.material.textfield.TextInputLayout

class SignUpController : BaseAuthController() {

    init {
        lock = false
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        caption.setText(R.string.sign_up_label)
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.color_sign_up))

        views = listOf(view.findViewById(R.id.email_input_edit),
                       view.findViewById(R.id.password_input_edit),
                       view.findViewById(R.id.confirm_password_edit)
        )
        views.forEach { editText ->
            if(editText.id == R.id.password_input_edit) {
                val inputLayout = view.findViewById<TextInputLayout>(R.id.password_input)
                val confirmLayout = view.findViewById<TextInputLayout>(R.id.confirm_password)
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                inputLayout.setTypeface(boldTypeface)
                confirmLayout.setTypeface(boldTypeface)

                editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        inputLayout.isPasswordVisibilityToggleEnabled = (editText.length() > 0)
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
                })

                editText.setOnFocusChangeListener { _, hasFocus ->
                    if(hasFocus) return@setOnFocusChangeListener
                    editText.isSelected = (editText.text!!.isNotEmpty())
                }
            }
        }

        caption.isVerticalText = true
        foldStuff()
        caption.translationX = getTextPadding()
    }

    private fun getTextPadding(): Float {
        return resources?.let {
            it.getDimension(R.dimen.folded_label_padding) / 2.1f
        } ?: 0f
    }

    private fun foldStuff() {
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, caption.textSize / 2f)
        caption.setTextColor(Color.WHITE)
        val params = ConstraintLayout.LayoutParams::class.java.cast(caption.layoutParams)
        params!!.rightToRight = ConstraintLayout.LayoutParams.UNSET
        params!!.verticalBias = 0.5f
        caption.layoutParams = params
    }


    override fun getAuthLayoutId(): Int = R.layout.sign_up_view

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun fold() {
        lock = true
        val rotateTransition = Rotate()
        rotateTransition.startAngle = 0f
        rotateTransition.endAngle = -90f
        rotateTransition.addTarget(caption)

        val transitionSet = TransitionSet().apply {
            duration = resources!!.getInteger(R.integer.duration).toLong()
            addTransition(ChangeBounds())
            addTransition(rotateTransition)
            addTransition(TextResize().addTarget(caption))
            ordering = TransitionSet.ORDERING_TOGETHER
            addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    super.onTransitionEnd(transition)
                    caption.translationX = getTextPadding()
                    caption.rotation = 0f
                    caption.isVerticalText = true
                    caption.requestLayout()
                    lock = false
                }
            })
        }
        TransitionManager.beginDelayedTransition(parent, transitionSet)
        foldStuff()
        caption.translationX = -caption.width / 8 + getTextPadding()

    }
}