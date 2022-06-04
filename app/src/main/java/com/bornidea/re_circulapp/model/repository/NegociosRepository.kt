package com.bornidea.re_circulapp.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bornidea.re_circulapp.model.APIService
import com.bornidea.re_circulapp.model.request.NegociosRequest
import com.bornidea.re_circulapp.model.response.NegociosResponse
import com.bornidea.re_circulapp.model.utils.RetrofitInstance

class NegociosRepository {
    fun registerUser(negociosRequest: NegociosRequest): LiveData<NegociosResponse> {
        val refService = RetrofitInstance
            .getRetrofitInstance()
            .create(APIService::class.java)

        val negocioResponse: LiveData<NegociosResponse> = liveData {
            val response = refService.getNegocios(negociosRequest)
            val bodyResponse = response.body()
            emit(bodyResponse ?: NegociosResponse(codigoRespuesta = 404))
        }
        return negocioResponse
    }
}