package com.bornidea.re_circulapp.model.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String = "",
    @SerializedName("correo")
    val correo: String = "",
    @SerializedName("edad")
    val edad: String = "",
    @SerializedName("estado")
    val estado: String = "",
    @SerializedName("genero")
    val genero: String = ""
)
