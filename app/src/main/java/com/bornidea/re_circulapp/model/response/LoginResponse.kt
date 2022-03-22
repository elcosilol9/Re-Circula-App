package com.bornidea.re_circulapp.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("codigo")
    val codigo: Int = 0,
    @SerializedName("mensaje")
    val mensaje: String = "",
    @SerializedName("usuario")
    val usuario: Usuario = Usuario()
)