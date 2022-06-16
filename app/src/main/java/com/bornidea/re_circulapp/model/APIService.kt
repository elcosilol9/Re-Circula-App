package com.bornidea.re_circulapp.model

import com.bornidea.re_circulapp.model.request.*
import com.bornidea.re_circulapp.model.response.EditProfileResponse
import com.bornidea.re_circulapp.model.response.LoginResponse
import com.bornidea.re_circulapp.model.response.NegociosResponse
import com.bornidea.re_circulapp.model.response.UpdateUserResponse
import com.bornidea.re_circulapp.model.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    //Inicio de sesion
    @POST(Constants.loginService)
    suspend fun verifyLogin(@Body login: LoginRequest): Response<LoginResponse>

    //Registro
    @POST(Constants.registerService)
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<LoginResponse>

    //Negocios
    @POST(Constants.negociosService)
    suspend fun getNegocios(@Body negociosRequest: NegociosRequest): Response<NegociosResponse>

    //EditarPerfil
    @POST(Constants.editprofile)
    suspend fun getInfoUser(@Body editProfileRequest: EditProfileRequest): Response<EditProfileResponse>

    //ActualizarPerfil
    @POST(Constants.updateProfile)
    suspend fun updateInfoUser(@Body updateUserRequest: UpdateUserRequest): Response<UpdateUserResponse>
}