package br.com.mirabilis.switcher

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by rodrigosimoesrosa on 10/04/19.
 * Copyright © 2019 Mirabilis. All rights reserved.
 */
object Switcher {

    const val ON = false
    const val ON_DEFAULT = "ON"
    const val OFF_DEFAULT = "OFF"
    const val BACKGROUND_OFF_COLOR_DEFAULT = android.R.color.white
    const val BACKGROUND_ON_COLOR_DEFAULT = R.color.colorAccent
    const val BORDER_BACKGROUND_OFF_COLOR_DEFAULT = R.color.named_switch_circle_toggle_shadow

    const val TEXT_ON_COLOR_DEFAULT = android.R.color.white
    const val TEXT_OFF_COLOR_DEFAULT = R.color.colorAccent
    const val ANIMATION_DURATION_DEFAULT = 250L
    const val POSITION_X_DEFAULT = 0f

    interface Listener {
        fun onSwitchTouched(id: Int, willBe: Boolean)
        fun onSwitchStatusChanged(id: Int, on: Boolean)
    }

    interface View {
        fun isOn(): Boolean
        fun setListener(listener: Listener)
    }

    @Parcelize
    class SwitchState(val state: Parcelable) : android.view.View.BaseSavedState(state) {
        var on: Boolean = ON
        var statusOnText: String = ON_DEFAULT
        var statusOffText: String = OFF_DEFAULT
        var backgroundOnColor: Int = BACKGROUND_ON_COLOR_DEFAULT
        var backgroundOffColor: Int = BACKGROUND_OFF_COLOR_DEFAULT
        var borderBackgroundOffColor: Int = BORDER_BACKGROUND_OFF_COLOR_DEFAULT
        var textOnColor: Int = TEXT_ON_COLOR_DEFAULT
        var textOffColor: Int = TEXT_OFF_COLOR_DEFAULT
    }
}
