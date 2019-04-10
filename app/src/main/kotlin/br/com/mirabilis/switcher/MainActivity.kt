package br.com.mirabilis.switcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by rodrigosimoesrosa on 10/04/19.
 * Copyright © 2019 Mirabilis. All rights reserved.
 */
class MainActivity : AppCompatActivity(), Switcher.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        labeledSwitch1.setListener(this)
        labeledSwitch2.setListener(this)
    }

    override fun onSwitchTouched(id: Int, willBe: Boolean) {
        when (id) {
            R.id.labeledSwitch1 -> showToast("First switcher touched")
            R.id.labeledSwitch2 -> showToast("Second switcher touched")
        }
    }

    override fun onSwitchStatusChanged(id: Int, on: Boolean) {
        when (id) {
            R.id.labeledSwitch1 -> showToast("First switcher changed")
            R.id.labeledSwitch2 -> showToast("Second switcher changed")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
