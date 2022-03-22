package com.bornidea.re_circulapp.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("correo")
    val correo: String = ""
)