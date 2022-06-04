package com.bornidea.re_circulapp.model.response

import com.google.gson.annotations.SerializedName

data class NegociosResponse(
    @SerializedName("codigoRespuesta")
    val codigoRespuesta: Int = 0,
    @SerializedName("negocios")
    val negocios: List<Negocios> = emptyList()
)
