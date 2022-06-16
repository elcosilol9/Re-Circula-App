package com.bornidea.re_circulapp.model.request

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("correo")
    val correo: String = "",
    @SerializedName("nombre")
    val nombre: String = "",
    @SerializedName("edad")
    val edad: Int = 0,
    @SerializedName("estado")
    val estado: String = ""
)
