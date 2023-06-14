package com.d3if3150.catatin.utils

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.showOrHide(isVisible: Boolean) = if (isVisible) show() else hide()
