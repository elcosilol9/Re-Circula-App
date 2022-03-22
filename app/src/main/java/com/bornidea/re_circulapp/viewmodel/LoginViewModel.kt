package com.bornidea.re_circulapp.viewmodel

import androidx.lifecycle.*
import com.bornidea.re_circulapp.model.repository.LoginRepository
import com.bornidea.re_circulapp.model.request.LoginRequest
import com.bornidea.re_circulapp.model.response.LoginResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    //Peticion a Servidor

    fun requestLogin(loginRequest: LoginRequest): LiveData<LoginResponse> =
        repository.getInfoUser(loginRequest)

}

class LoginViewModelFactory(private val repository: LoginRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(repository) as T
        throw IllegalArgumentException("Se desconoce el ViewModel")
    }
}