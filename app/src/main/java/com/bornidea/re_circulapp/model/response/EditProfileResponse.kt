package com.bornidea.re_circulapp.model.response

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("codigoRespuesta")
    val codigoRespuesta: Int = 0,
    @SerializedName("nombre")
    val nombre: String = "",
    @SerializedName("edad")
    val edad: String = "",
    @SerializedName("estado")
    val estado: String = ""
)
