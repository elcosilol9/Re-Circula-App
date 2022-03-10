package com.bornidea.re_circulapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityRegistroBinding

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
            startActivity(Intent(this, RegistroDetalle::class.java))
        }
    }
}