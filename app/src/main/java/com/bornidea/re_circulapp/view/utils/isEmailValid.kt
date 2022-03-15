package com.bornidea.re_circulapp.view.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

fun isEmailValid(email: CharSequence): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(email)
    return matcher.matches()
}