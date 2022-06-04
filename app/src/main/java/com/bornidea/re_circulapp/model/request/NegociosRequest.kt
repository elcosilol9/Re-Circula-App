package com.bornidea.re_circulapp.model.request

import com.google.gson.annotations.SerializedName

data class NegociosRequest(
    @SerializedName("producto")
    val producto: String = "",
    @SerializedName("servicio")
    val servicio: String = "",
    @SerializedName("localidad")
    val localidad: String = "",
)
