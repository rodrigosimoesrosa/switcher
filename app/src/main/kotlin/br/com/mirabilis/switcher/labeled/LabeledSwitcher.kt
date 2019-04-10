package br.com.mirabilis.switcher.labeled

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import br.com.mirabilis.switcher.R
import br.com.mirabilis.switcher.Switcher
import kotlinx.android.synthetic.main.view_named_switch.view.*

/**
 * Created by rodrigosimoesrosa on 10/04/19.
 * Copyright © 2019 Mirabilis. All rights reserved.
 */
class LabeledSwitcher : ConstraintLayout, Switcher.View {

    private var listener: Switcher.Listener? = null

    private var on: Boolean
    private var statusOnText: String
    private var statusOffText: String

    private var backgroundOnColor: Int
    private var backgroundOffColor: Int
    private var borderBackgroundOffColor: Int

    private var textOnColor: Int
    private var textOffColor: Int

    private var parentDrawable: LayerDrawable? = null
    private var layerDrawableShadow: GradientDrawable? = null
    private var layerDrawable: GradientDrawable? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null,
                defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {

        val attributes = context.theme.obtainStyledAttributes(
                attrs, R.styleable.LabeledSwitcher, 0, 0)

        statusOnText = attributes.getString(R.styleable.LabeledSwitcher_labeledSwitchStatusOnText) ?: Switcher.ON_DEFAULT
        statusOffText = attributes.getString(R.styleable.LabeledSwitcher_labeledSwitchStatusOffText) ?: Switcher.OFF_DEFAULT
        on = attributes.getBoolean(R.styleable.LabeledSwitcher_labeledSwitchON, Switcher.ON)

        backgroundOffColor = attributes.getColor(R.styleable.LabeledSwitcher_labeledSwitchBackgroundOffColor,
                ContextCompat.getColor(context, Switcher.BACKGROUND_OFF_COLOR_DEFAULT))

        borderBackgroundOffColor = attributes.getColor(R.styleable.LabeledSwitcher_labeledSwitchBorderBackgroundOffColor,
                ContextCompat.getColor(context, Switcher.BORDER_BACKGROUND_OFF_COLOR_DEFAULT))

        backgroundOnColor = attributes.getColor(R.styleable.LabeledSwitcher_labeledSwitchBackgroundOnColor,
                ContextCompat.getColor(context, Switcher.BACKGROUND_ON_COLOR_DEFAULT))

        textOffColor = attributes.getColor(R.styleable.LabeledSwitcher_labeledSwitchOffTextColor,
                ContextCompat.getColor(context, Switcher.TEXT_OFF_COLOR_DEFAULT))

        textOnColor = attributes.getColor(R.styleable.LabeledSwitcher_labeledSwitchOnTextColor,
                ContextCompat.getColor(context, Switcher.TEXT_ON_COLOR_DEFAULT))

        applyText()
    }

    private fun applyText() {
        lblOnTitle.text = statusOnText
        lblOffTitle.text = statusOffText

        lblOnTitle.setTextColor(textOnColor)
        lblOffTitle.setTextColor(textOffColor)

        applyTextState()
    }

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_named_switch, this, true)

        parentDrawable = backgroundView.background as LayerDrawable
        layerDrawableShadow = parentDrawable?.findDrawableByLayerId(
                R.id.nameSwitchDrawableBackgroundShadow) as GradientDrawable
        layerDrawable = parentDrawable?.findDrawableByLayerId(
                R.id.nameSwitchDrawableBackground) as GradientDrawable

        setOnClickListener {
            isClickable = false
            changeStatus()
        }

        var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
        onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            backgroundView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
            applyState(true)
        }

        backgroundView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    override fun onSaveInstanceState(): Parcelable {
        val state: Parcelable = super.onSaveInstanceState()
        val switchState = Switcher.SwitchState(state)
        switchState.on = on
        switchState.statusOnText = statusOnText
        switchState.statusOffText = statusOffText
        switchState.backgroundOnColor = backgroundOnColor
        switchState.backgroundOffColor = backgroundOffColor
        switchState.borderBackgroundOffColor = borderBackgroundOffColor
        switchState.textOnColor = textOnColor
        switchState.textOffColor = textOffColor

        return switchState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val switchState = state as Switcher.SwitchState
        super.onRestoreInstanceState(switchState.superState)
        on = switchState.on
        statusOnText = switchState.statusOnText
        statusOffText = switchState.statusOffText
        backgroundOnColor = switchState.backgroundOnColor
        backgroundOffColor = switchState.backgroundOffColor
        borderBackgroundOffColor = switchState.borderBackgroundOffColor
        textOnColor = switchState.textOnColor
        textOffColor = switchState.textOffColor
    }

    private fun getBiggestText(): String? {
        if (statusOnText.length > statusOffText.length) {
            return statusOnText
        }

        return statusOffText
    }

    private fun applyState(initial: Boolean = false) {
        applyTextState()

        if (isInEditMode) {
            applyBackgroundState(false)
            applyToggleState(false)
            return
        }

        if (initial) {
            applyBackgroundState(false)
            applyToggleState(false)
            return
        }

        applyBackgroundState(true)
        applyToggleState(true)
    }

    private fun applyBackgroundState(hasAnimation: Boolean) {
        if (isOn()) {
            if (hasAnimation) {
                animateColorForDrawable(layerDrawableShadow, borderBackgroundOffColor, backgroundOnColor)
                animateColorForDrawable(layerDrawable, backgroundOffColor, backgroundOnColor)
                return
            }

            layerDrawableShadow?.setColor(backgroundOnColor)
            layerDrawable?.setColor(backgroundOnColor)
            return
        }

        if (hasAnimation) {
            animateColorForDrawable(layerDrawableShadow, backgroundOnColor, borderBackgroundOffColor)
            animateColorForDrawable(layerDrawable, backgroundOnColor, backgroundOffColor)
            return
        }

        layerDrawableShadow?.setColor(borderBackgroundOffColor)
        layerDrawable?.setColor(backgroundOffColor)
    }

    private fun applyToggleState(hasAnimation: Boolean) {
        if (isOn()) {
            val positionXTo = backgroundView.measuredWidth - (toggle.measuredWidth)

            if (hasAnimation) {
                animatePositionXToggle(positionXTo.toFloat())
                return
            }

            toggle.x = positionXTo.toFloat()
            return
        }

        val positionXTo = Switcher.POSITION_X_DEFAULT

        if (hasAnimation) {
            animatePositionXToggle(positionXTo)
            return
        }

        toggle.x = positionXTo
    }

    private fun animatePositionXToggle(positionXTo: Float) {
        toggle.animate().translationX(positionXTo).setDuration(Switcher.ANIMATION_DURATION_DEFAULT).start()
    }

    private fun animateColorForDrawable(drawable: GradientDrawable?, colorFrom: Int, colorTo: Int) {
        if (drawable == null) {
            return
        }

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = Switcher.ANIMATION_DURATION_DEFAULT
        colorAnimation.addUpdateListener {
            val color: Int = it.animatedValue as Int
            drawable.setColor(color)
        }

        colorAnimation.start()
    }

    private fun applyTextState() {
        if (isOn()) {
            lblOnTitle.visibility = View.VISIBLE
            lblOffTitle.visibility = View.INVISIBLE
            return
        }

        lblOnTitle.visibility = View.INVISIBLE
        lblOffTitle.visibility = View.VISIBLE
    }

    private fun changeStatus() {
        on = !on
        listener?.onSwitchTouched(id, isOn())
        applyState()

        postDelayed({
            isClickable = true
            listener?.onSwitchStatusChanged(id, isOn())
        }, Switcher.ANIMATION_DURATION_DEFAULT)
    }

    fun setOn(on: Boolean) {
        this.on = on

        applyState(true)
    }

    override fun isOn(): Boolean {
        return on
    }

    override fun setListener(listener: Switcher.Listener) {
        this.listener = listener
    }
}
