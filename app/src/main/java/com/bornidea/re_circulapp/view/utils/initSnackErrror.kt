package com.bornidea.re_circulapp.view.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.bornidea.re_circulapp.R
import com.google.android.material.snackbar.Snackbar

fun initSnackError(view: View, context: Context, mensaje: String) {
    Snackbar.make(view, mensaje, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(
            ContextCompat.getColor(context, R.color.red_error)
        )
        .setTextColor(
            ContextCompat.getColor(context, R.color.white)
        )
        .show()
}