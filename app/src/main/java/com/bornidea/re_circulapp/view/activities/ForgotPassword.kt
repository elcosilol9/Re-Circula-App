package com.bornidea.re_circulapp.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityForgotPasswordBinding
import com.bornidea.re_circulapp.view.utils.hideSoftKeyboard
import com.bornidea.re_circulapp.view.utils.initSnackError
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        supportActionBar?.hide()

        binding.apply {
            btRegistrar.setOnClickListener {
                var correo = textEditMail.text.toString()
                correo = correo.replace(" ", "")
                hideSoftKeyboard(this@ForgotPassword)
                Firebase.auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(
                                container,
                                getString(R.string.response_recovery_pass),
                                Snackbar.LENGTH_LONG
                            ).show()
                            btRegistrar.isEnabled = false
                            btRegistrar.isClickable = false
                        } else {
                            initSnackError(
                                container,
                                applicationContext,
                                getString(R.string.error_recovery_pass)
                            )
                        }
                    }
            }
        }
    }
}