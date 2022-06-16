package com.bornidea.re_circulapp.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bornidea.re_circulapp.model.APIService
import com.bornidea.re_circulapp.model.request.EditProfileRequest
import com.bornidea.re_circulapp.model.request.UpdateUserRequest
import com.bornidea.re_circulapp.model.response.EditProfileResponse
import com.bornidea.re_circulapp.model.response.UpdateUserResponse
import com.bornidea.re_circulapp.model.utils.RetrofitInstance

class EditProfileRepository {
    fun getInfoUser(editProfileRequest: EditProfileRequest): LiveData<EditProfileResponse> {
        val refService = RetrofitInstance
            .getRetrofitInstance()
            .create(APIService::class.java)

        val responseLogin: LiveData<EditProfileResponse> = liveData {
            val response = refService.getInfoUser(editProfileRequest)
            val bodyResponse = response.body()
            emit(bodyResponse ?: EditProfileResponse(codigoRespuesta = 404))
        }
        return responseLogin
    }

    fun updateUser(updateUserRequest: UpdateUserRequest): LiveData<UpdateUserResponse> {
        val refService = RetrofitInstance
            .getRetrofitInstance()
            .create(APIService::class.java)

        val responseLogin: LiveData<UpdateUserResponse> = liveData {
            val response = refService.updateInfoUser(updateUserRequest)
            val bodyResponse = response.body()
            emit(bodyResponse ?: UpdateUserResponse(codigoRespuesta = 404))
        }
        return responseLogin
    }
}