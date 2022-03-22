package com.bornidea.re_circulapp.model

import com.bornidea.re_circulapp.model.request.LoginRequest
import com.bornidea.re_circulapp.model.response.LoginResponse
import com.bornidea.re_circulapp.model.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    //Inicio de sesion
    @POST(Constants.loginService)
    suspend fun verifyLogin(@Body login:LoginRequest): Response<LoginResponse>
}