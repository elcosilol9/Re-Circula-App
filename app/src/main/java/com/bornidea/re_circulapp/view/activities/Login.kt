package com.bornidea.re_circulapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityLoginBinding
import com.bornidea.re_circulapp.model.repository.LoginRepository
import com.bornidea.re_circulapp.model.request.LoginRequest
import com.bornidea.re_circulapp.model.utils.isOnline
import com.bornidea.re_circulapp.view.utils.hideSoftKeyboard
import com.bornidea.re_circulapp.view.utils.initSnackError
import com.bornidea.re_circulapp.view.utils.isEmailValid
import com.bornidea.re_circulapp.viewmodel.LoginViewModel
import com.bornidea.re_circulapp.viewmodel.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    /**Antereiormente onActivityResult*/
    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val correo = account.email ?: ""
                            PreferencesLogin(correo, "completo")
                            //TODO REDIRECCIONAR AL MENU PRINCIPAL
                            binding.constraintProgress.visibility = View.GONE
                            initLogin(correo)
                        } else {
                            binding.constraintProgress.visibility = View.GONE
                            initSnackError(
                                binding.container,
                                applicationContext,
                                getString(R.string.error_signin)
                            )
                            binding.constraintProgress.visibility = View.GONE
                        }
                    }
            } catch (e: ApiException) {
                binding.constraintProgress.visibility = View.GONE
                initSnackError(
                    binding.container,
                    applicationContext,
                    getString(R.string.error_signin)
                )
                binding.constraintProgress.visibility = View.GONE
            }
        }

    //Firebase
    lateinit var auth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ReCirculapp)
        super.onCreate(savedInstanceState)
        /**DataBinding*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        /**Quitar ActionBar*/
        supportActionBar?.hide()

        /**Inicializar ViewModel*/
        val repository = LoginRepository()
        val factory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        /**Inicializar Firebase Auth*/
        auth = Firebase.auth
        binding.btRegistro.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }
        binding.tvForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        binding.btEntrar.setOnClickListener {
            if (isOnline(this)) {
                if (verifyContent()) {
                    binding.constraintProgress.visibility = View.VISIBLE
                    /**Contenido Válido*/
                    val correo = binding.textEditMail.text.toString().trim()
                    val pass = binding.textEditPass.text.toString().trim()
                    hideSoftKeyboard(this)
                    SignIn(correo, pass)
                }
            } else {
                initSnackError(binding.container, this, getString(R.string.error_network))
            }
        }
        binding.btGoogle.setOnClickListener {
            if (isOnline(this)) {
                binding.constraintProgress.visibility = View.VISIBLE
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(this, gso)
                responseLauncher.launch(googleSignInClient.signInIntent)
            } else {
                initSnackError(binding.container, this, getString(R.string.error_network))
            }
        }
    }

    /**Login con correo y contraseña*/
    private fun SignIn(correo: String, pass: String) {
        auth.signInWithEmailAndPassword(correo, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    PreferencesLogin(correo, "completo")
                    //TODO REDIRECCIONAR AL MENU PRINCIPAL
                    binding.constraintProgress.visibility = View.GONE
                    initLogin(correo)
                } else {
                    if (task.exception is FirebaseAuthInvalidUserException) {
                        initSnackError(
                            binding.container,
                            applicationContext,
                            getString(R.string.not_register)
                        )
                        binding.constraintProgress.visibility = View.GONE
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

    /**Verificar si existe el usuario en servidor*/

    private fun initLogin(correo: String) {
        loginViewModel.requestLogin(LoginRequest(correo = correo))
            .observe(this, Observer { response ->
                when (response.codigo) {
                    200 -> {
                        /**Usuario Existe*/
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                    }
                    201 -> {
                        /**Es un usuario nuevo*/
                        initSnackError(binding.container, baseContext, getString(R.string.txt_201))
                    }
                    404 -> {
                        /**Fallo al conectar con el servidor*/
                        initSnackError(binding.container, baseContext, getString(R.string.txt_404))
                    }
                }
            })
    }

    /**Guardar informacion en preferencias*/
    private fun PreferencesLogin(correo: String, status: String) {
        val preferences = getSharedPreferences("Login", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("correo", correo)
        editor.putString("status", status)
        editor.apply()
    }

    /**Verificar que no existan campos vacíos*/
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