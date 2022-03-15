package com.bornidea.re_circulapp.view.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun hideSoftKeyboard(activity: Activity) {
    val inputMethodManager: InputMethodManager =
        activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    if(inputMethodManager.isAcceptingText){
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }
}