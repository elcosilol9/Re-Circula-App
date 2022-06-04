package com.bornidea.re_circulapp.viewmodel

import androidx.lifecycle.*
import com.bornidea.re_circulapp.model.repository.NegociosRepository
import com.bornidea.re_circulapp.model.repository.RegisterRepository
import com.bornidea.re_circulapp.model.request.NegociosRequest
import com.bornidea.re_circulapp.model.response.NegociosResponse
import java.lang.IllegalArgumentException

class NegociosViewModel(private val repository: NegociosRepository) : ViewModel() {

    //Peticion a Servidor

    fun requestNegocios(negociosRequest: NegociosRequest): LiveData<NegociosResponse> =
        repository.registerUser(negociosRequest)

}

class NegociosViewModelFactory(private val repository: NegociosRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NegociosViewModel::class.java))
            return NegociosViewModel(repository) as T
        throw IllegalArgumentException("Se desconoce el ViewModel")
    }
}