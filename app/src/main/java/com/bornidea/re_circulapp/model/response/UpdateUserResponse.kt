package com.bornidea.re_circulapp.model.response

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(
    @SerializedName("codigoRespuesta")
    val codigoRespuesta: Int = 0
)
