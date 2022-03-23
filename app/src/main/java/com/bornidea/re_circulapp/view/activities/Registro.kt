package com.bornidea.re_circulapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityRegistroBinding
import com.bornidea.re_circulapp.view.utils.hideSoftKeyboard
import com.bornidea.re_circulapp.view.utils.initSnackError
import com.bornidea.re_circulapp.view.utils.isEmailValid

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registro)
        supportActionBar?.hide()

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
                Register(correo, pass)
            }
        }
    }

    private fun Register(correo: String, pass: String) {

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