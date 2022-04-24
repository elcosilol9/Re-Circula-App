package com.bornidea.re_circulapp.viewmodel

import androidx.lifecycle.*
import com.bornidea.re_circulapp.model.repository.LoginRepository
import com.bornidea.re_circulapp.model.repository.RegisterRepository
import com.bornidea.re_circulapp.model.request.LoginRequest
import com.bornidea.re_circulapp.model.request.RegisterRequest
import com.bornidea.re_circulapp.model.response.LoginResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {

    //Peticion a Servidor

    fun requestLogin(registerRequest: RegisterRequest): LiveData<LoginResponse> =
        repository.registerUser(registerRequest)

}

class RegisterViewModelFactory(private val repository: RegisterRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java))
            return RegisterViewModel(repository) as T
        throw IllegalArgumentException("Se desconoce el ViewModel")
    }
}