package com.bornidea.re_circulapp.model.response

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("correo")
    val correo: String = "",
    @SerializedName("edad")
    val edad: Int = 0,
    @SerializedName("estado")
    val estado: String = "",
    @SerializedName("genero")
    val genero: String = "",
    @SerializedName("nombre")
    val nombre: String = ""
)