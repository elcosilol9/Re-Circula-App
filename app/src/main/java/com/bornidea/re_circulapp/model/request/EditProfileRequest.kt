package com.bornidea.re_circulapp.model.request

import com.google.gson.annotations.SerializedName

data class EditProfileRequest(
    @SerializedName("correo")
    val correo: String = ""
)
