package com.bornidea.re_circulapp.view.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityEditProfileBinding
import com.bornidea.re_circulapp.model.repository.EditProfileRepository
import com.bornidea.re_circulapp.model.request.EditProfileRequest
import com.bornidea.re_circulapp.model.request.UpdateUserRequest
import com.bornidea.re_circulapp.viewmodel.EditProfileViewModel
import com.bornidea.re_circulapp.viewmodel.EditProfileViewModelFactory

class EditProfileActivity : AppCompatActivity() {


    private var preferences: SharedPreferences? = null
    private lateinit var editprofileViewModel: EditProfileViewModel
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        supportActionBar?.hide()

        /**Inicializar ViewModel*/
        val repository = EditProfileRepository()
        val factory = EditProfileViewModelFactory(repository)
        editprofileViewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]

        preferences =
            getSharedPreferences("Login", MODE_PRIVATE)
        getInfoUser(preferences?.getString("correo", "") ?: "")

        actualizarUser()
    }

    private fun actualizarUser() {
        binding.apply {
            btUpdate.setOnClickListener {
                binding.progress.visibility = View.VISIBLE
                binding.container.visibility = View.GONE
                val updateUserRequest = UpdateUserRequest(
                    correo = preferences?.getString("correo", "") ?: "",
                    nombre = textEditName.text.toString(),
                    edad = textEditEdad.text.toString().toInt(),
                    estado = autoCompleteEstados.text.toString()
                )
                editprofileViewModel.updateDataUser(updateUserRequest)
                    .observe(this@EditProfileActivity, Observer { response ->
                        when (response.codigoRespuesta) {
                            200 -> {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Datos Actualizados",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackPressed()
                            }
                            else -> {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "No pudimos establecer conexion con el servidor",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackPressed()
                            }
                        }
                    })
            }
        }
    }

    private fun getInfoUser(correo: String) {
        binding.progress.visibility = View.GONE
        binding.container.visibility = View.VISIBLE
        val editProfileRequest = EditProfileRequest(correo)
        editprofileViewModel.getDataUser(editProfileRequest)
            .observe(this, Observer { response ->
                when (response.codigoRespuesta) {
                    200 -> {
                        binding.apply {
                            textEditName.setText(response.nombre)
                            textEditEdad.setText(response.edad)
                            setArrayEstados()
                            autoCompleteEstados.setText(response.estado, false)
                        }
                    }
                    else -> {
                        Toast.makeText(
                            this,
                            "No pudimos establecer conexion con el servidor",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        setArrayEstados()
    }

    private fun setArrayEstados() {
        val estados = resources.getStringArray(R.array.estados)
        val arrayAdapter = ArrayAdapter(this, R.layout.drop_down_item, estados)
        binding.autoCompleteEstados.setAdapter(arrayAdapter)
    }
}