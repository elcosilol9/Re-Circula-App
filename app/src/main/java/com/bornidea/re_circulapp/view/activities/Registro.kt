package com.bornidea.re_circulapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityRegistroBinding
import com.bornidea.re_circulapp.model.repository.LoginRepository
import com.bornidea.re_circulapp.model.request.LoginRequest
import com.bornidea.re_circulapp.view.utils.hideSoftKeyboard
import com.bornidea.re_circulapp.view.utils.initSnackError
import com.bornidea.re_circulapp.view.utils.isEmailValid
import com.bornidea.re_circulapp.viewmodel.LoginViewModel
import com.bornidea.re_circulapp.viewmodel.LoginViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Registro : AppCompatActivity() {

    companion object {
        const val CORREO = "CORREO"
        const val PASS = "PASS"
    }

    lateinit var auth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registro)
        supportActionBar?.hide()

        /**Inicializar ViewModel*/
        val repository = LoginRepository()
        val factory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        /**Inicializar Firebase Auth*/
        auth = Firebase.auth

        initView()

    }

    private fun initView() {
        binding.btIniciarSesion.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        binding.tvForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
        binding.btRegistrar.setOnClickListener {
            //startActivity(Intent(this, RegistroDetalle::class.java))
            if (verifyContent()) {
                binding.constraintProgress.visibility = View.VISIBLE
                /**Contenido Válido*/
                val correo = binding.textEditMail.text.toString().trim()
                val pass = binding.textEditPass.text.toString().trim()
                hideSoftKeyboard(this)
                SignIn(correo, pass)

                //Register(correo, pass)
            }
        }
    }

    private fun SignIn(correo: String, pass: String) {
        auth.signInWithEmailAndPassword(correo, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    initSnackError(
                        binding.container,
                        applicationContext,
                        getString(R.string.is_register)
                    )
                    binding.constraintProgress.visibility = View.GONE
                } else {
                    if (task.exception is FirebaseAuthInvalidUserException) {
                        binding.constraintProgress.visibility = View.GONE
                        val intent = Intent(this, RegistroDetalle::class.java)
                        intent.putExtra(CORREO, binding.textEditMail.text.toString().trim())
                        intent.putExtra(PASS, binding.textEditPass.text.toString().trim())
                        startActivity(intent)
                    } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        initSnackError(
                            binding.container,
                            applicationContext,
                            getString(R.string.incorrect_pass)
                        )
                        binding.constraintProgress.visibility = View.GONE
                    }
                }
            }
    }

    private fun verifyContent(): Boolean {
        return if (binding.textEditMail.text.toString()
                .isBlank() && binding.textEditMail.text.toString().isEmpty()
        ) {
            initSnackError(binding.container, this, "Ingrese un correo")
            false
        } else if (binding.textEditPass.text.toString()
                .isBlank() && binding.textEditPass.text.toString().isEmpty()
        ) {
            initSnackError(binding.container, this, "Ingrese una contraseña")
            false
        } else {
            if (isEmailValid(binding.textEditMail.text.toString())) {
                /**Contraseña y correo completos*/
                true
            } else {
                initSnackError(binding.container, this, "Ingrese un correo válido")
                false
            }
        }
    }
}