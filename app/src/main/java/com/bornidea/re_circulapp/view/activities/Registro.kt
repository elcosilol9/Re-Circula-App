package com.bornidea.re_circulapp.view.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityRegistroBinding
import com.bornidea.re_circulapp.model.repository.LoginRepository
import com.bornidea.re_circulapp.model.request.LoginRequest
import com.bornidea.re_circulapp.model.utils.Constants
import com.bornidea.re_circulapp.model.utils.Constants.METODO
import com.bornidea.re_circulapp.model.utils.Constants.USER
import com.bornidea.re_circulapp.model.utils.isOnline
import com.bornidea.re_circulapp.view.utils.hideSoftKeyboard
import com.bornidea.re_circulapp.view.utils.initSnackError
import com.bornidea.re_circulapp.view.utils.isEmailValid
import com.bornidea.re_circulapp.viewmodel.LoginViewModel
import com.bornidea.re_circulapp.viewmodel.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Registro : AppCompatActivity() {

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
                            PreferencesLogin(correo)
                            //TODO REDIRECCIONAR AL MENU PRINCIPAL
                            binding.constraintProgress.visibility = View.GONE
                            initRegisterGoogle(correo)
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

    companion object {
        const val CORREO = "CORREO"
        const val PASS = "PASS"
        const val METODO_EMAIL = "METODO_EMAIL"
        const val METODO_GOOGLE = "METODO_GOOGLE"
    }

    lateinit var auth: FirebaseAuth
    private var preferences: SharedPreferences? = null
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
            if (verifyContent()) {
                binding.constraintProgress.visibility = View.VISIBLE
                /**Contenido Válido*/
                val correo = binding.textEditMail.text.toString().trim()
                val pass = binding.textEditPass.text.toString().trim()
                hideSoftKeyboard(this)
                SignIn(correo, pass)
            }
        }
        binding.btGoogle.setOnClickListener {
            if (isOnline(this)) {
                saveMethod(Registro.METODO_GOOGLE)
                binding.constraintProgress.visibility = View.VISIBLE
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    //Apk
                    //.requestIdToken("283788184596-r70ghpt5jnt0ofm9t0rjm1g53vamla3t.apps.googleusercontent.com")
                    //Debug
                    //.requestIdToken("283788184596-3k0s914e4797qipp57akk62kota6c79s.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(this, gso)
                responseLauncher.launch(googleSignInClient.signInIntent)
            } else {
                initSnackError(binding.container, this, getString(R.string.error_network))
            }
        }
    }


    private fun initRegisterGoogle(correo: String) {
        loginViewModel.requestLogin(LoginRequest(correo = correo))
            .observe(this, Observer { response ->
                when (response.codigo) {
                    200 -> {
                        initSnackError(binding.container, baseContext, getString(R.string.is_register))
                    }
                    201 -> {
                        /**Es un usuario nuevo*/
                        saveMethod(METODO_EMAIL)
                        binding.constraintProgress.visibility = View.GONE
                        val intent = Intent(this, RegistroDetalle::class.java)
                        intent.putExtra(CORREO, correo)
                        intent.putExtra(PASS, "Recirculapp12")
                        startActivity(intent)
                    }
                    404 -> {
                        /**Fallo al conectar con el servidor*/
                        initSnackError(binding.container, baseContext, getString(R.string.txt_404))
                    }
                }
            })
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
                        saveMethod(METODO_EMAIL)
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

    private fun saveMethod(metodoEmail: String) {
        val preferences = getSharedPreferences(USER, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(METODO, metodoEmail)
        editor.apply()
    }

    /**Guardar informacion en preferencias*/
    private fun PreferencesLogin(correo: String) {
        val preferences = getSharedPreferences("Login", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("correo", correo)
        editor.apply()
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
            if (isEmailValid(binding.textEditMail.text.toString().trim())) {
                /**Contraseña y correo completos*/
                true
            } else {
                initSnackError(binding.container, this, "Ingrese un correo válido")
                false
            }
        }
    }
}