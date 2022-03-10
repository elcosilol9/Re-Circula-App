package com.bornidea.re_circulapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ReCirculapp)
        super.onCreate(savedInstanceState)
        /**DataBinding*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        /**Quitar ActionBar*/
        supportActionBar?.hide()

        binding.btRegistro.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }
        binding.tvForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }
}