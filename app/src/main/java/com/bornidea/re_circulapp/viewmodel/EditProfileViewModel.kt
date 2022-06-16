package com.bornidea.re_circulapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bornidea.re_circulapp.model.repository.EditProfileRepository
import com.bornidea.re_circulapp.model.request.EditProfileRequest
import com.bornidea.re_circulapp.model.request.UpdateUserRequest
import com.bornidea.re_circulapp.model.response.EditProfileResponse
import com.bornidea.re_circulapp.model.response.UpdateUserResponse
import java.lang.IllegalArgumentException

class EditProfileViewModel(private val repository: EditProfileRepository) :
    ViewModel() {
    fun getDataUser(editProfileRequest: EditProfileRequest): LiveData<EditProfileResponse> =
        repository.getInfoUser(editProfileRequest)

    fun updateDataUser(updateUserRequest: UpdateUserRequest): LiveData<UpdateUserResponse> =
        repository.updateUser(updateUserRequest)
}

class EditProfileViewModelFactory(private val repository: EditProfileRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java))
            return EditProfileViewModel(repository) as T
        throw IllegalArgumentException("Se desconoce el ViewModel")
    }
}