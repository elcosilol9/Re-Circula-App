package com.bornidea.re_circulapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityRegistroDetalleBinding
import com.bornidea.re_circulapp.model.repository.RegisterRepository
import com.bornidea.re_circulapp.model.request.RegisterRequest
import com.bornidea.re_circulapp.model.response.LoginResponse
import com.bornidea.re_circulapp.model.utils.Constants
import com.bornidea.re_circulapp.model.utils.Constants.EDAD
import com.bornidea.re_circulapp.model.utils.Constants.ESTADO
import com.bornidea.re_circulapp.model.utils.Constants.GENERO
import com.bornidea.re_circulapp.model.utils.Constants.ISACTIVE
import com.bornidea.re_circulapp.model.utils.Constants.NOMBRE
import com.bornidea.re_circulapp.model.utils.Constants.USER
import com.bornidea.re_circulapp.view.activities.Registro.Companion.CORREO
import com.bornidea.re_circulapp.view.activities.Registro.Companion.PASS
import com.bornidea.re_circulapp.view.utils.hideSoftKeyboard
import com.bornidea.re_circulapp.view.utils.initSnackError
import com.bornidea.re_circulapp.viewmodel.RegisterViewModel
import com.bornidea.re_circulapp.viewmodel.RegisterViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistroDetalle : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private var correo: String? = null
    private var pass: String? = null
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegistroDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registro_detalle)
        supportActionBar?.hide()

        /**Inicializar Firebase Auth*/
        auth = Firebase.auth

        /**Inicializar ViewModel*/
        val repository = RegisterRepository()
        val factory = RegisterViewModelFactory(repository)
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        getIntentForView()
        initView()
    }

    override fun onResume() {
        super.onResume()
        setArrayEstados()
        setArrayGeneros()
    }

    private fun setArrayGeneros() {
        val generos = resources.getStringArray(R.array.generos)
        val arrayAdapter = ArrayAdapter(this, R.layout.drop_down_item, generos)
        binding.autoCompleteGenero.setAdapter(arrayAdapter)
    }

    private fun setArrayEstados() {
        val estados = resources.getStringArray(R.array.estados)
        val arrayAdapter = ArrayAdapter(this, R.layout.drop_down_item, estados)
        binding.autoCompleteEstados.setAdapter(arrayAdapter)
    }

    private fun initView() {
        binding.apply {
            btRegistrar.setOnClickListener {
                hideSoftKeyboard(this@RegistroDetalle)
                //TODO REVISAR VERIFICACION CONEXION A INTERNET
                if (VerifyContent()) {
                    progress.visibility = View.VISIBLE
                    if (pass == "Recirculapp12") {
                        Register(correo ?: "")
                    } else {
                        RegistrarUsuario(correo ?: "", pass ?: "Recirculapp12")
                    }
                }
            }
        }
    }

    private fun RegistrarUsuario(correo: String, pass: String) {
        auth.createUserWithEmailAndPassword(correo, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.progress.visibility = View.GONE
                    Register(correo)
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        binding.progress.visibility = View.GONE
                        initSnackError(
                            binding.container,
                            applicationContext,
                            getString(R.string.is_register)
                        )
                    } else {
                        binding.progress.visibility = View.GONE
                        initSnackError(
                            binding.container,
                            applicationContext,
                            getString(R.string.error_register)
                        )
                    }
                }
            }
    }

    private fun Register(correo: String) {
        val registerRequest = RegisterRequest(
            nombre = binding.textEditName.text.toString(),
            correo = correo,
            edad = binding.textEditEdad.text.toString(),
            estado = binding.autoCompleteEstados.text.toString(),
            genero = binding.autoCompleteGenero.text.toString()
        )
        registerViewModel.requestLogin(registerRequest)
            .observe(this, Observer { response ->
                when (response.codigo) {
                    200 -> {
                        /**Usuario Registrado*/
                        saveUser(response)
                        startActivity(Intent(this, MenuActivity::class.java))
                    }
                    201 -> {
                        /**Error al registrar usuario */
                        Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                    404 -> {
                        //TODO BORRAR CORREO DE GOOGLE
                        /**Fallo al conectar con el servidor*/
                        initSnackError(
                            binding.container,
                            baseContext,
                            getString(R.string.txt_404)
                        )
                    }
                }
            })
    }

    private fun saveUser(response: LoginResponse) {
        val preferences = getSharedPreferences(USER, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(Constants.CORREO, response.usuario.correo)
        editor.putString(NOMBRE, response.usuario.nombre)
        editor.putString(ESTADO, response.usuario.estado)
        editor.putString(GENERO, response.usuario.genero)
        editor.putString(EDAD, response.usuario.edad.toString())
        editor.putBoolean(ISACTIVE, true)
        editor.apply()
    }

    private fun getIntentForView() {
        val intent = intent.extras
        if (intent != null) {
            correo = intent.getString(CORREO)
            pass = intent.getString(PASS)
        }
    }

    private fun VerifyContent(): Boolean {
        binding.apply {
            textInputGenero.error = null
            textInputEstado.error = null
            textInputEdad.error = null
            textInputName.error = null
            return if (textEditName.text!!.isNotBlank() && textEditName.text!!.isNotEmpty()) {
                if (textEditEdad.text!!.isNotBlank() && textEditEdad.text!!.isNotEmpty()) {
                    if (autoCompleteEstados.text!!.isNotBlank() && autoCompleteEstados.text!!.isNotEmpty()) {
                        if (autoCompleteGenero.text!!.isNotBlank() && autoCompleteGenero.text!!.isNotEmpty()) {
                            true
                        } else {
                            textInputGenero.error = "Ingrese su Género"
                            false
                        }
                    } else {
                        textInputEstado.error = "Ingrese su Estado"
                        false
                    }
                } else {
                    textInputEdad.error = "Ingrese su edad"
                    false
                }
            } else {
                textInputName.error = "Ingrese su nombre"
                false
            }
        }
    }

}