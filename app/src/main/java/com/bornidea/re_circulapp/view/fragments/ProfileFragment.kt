package com.bornidea.re_circulapp.view.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bornidea.re_circulapp.BuildConfig
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.FragmentProfileBinding
import com.bornidea.re_circulapp.model.repository.EditProfileRepository
import com.bornidea.re_circulapp.model.request.EditProfileRequest
import com.bornidea.re_circulapp.model.utils.Constants.METODO
import com.bornidea.re_circulapp.model.utils.Constants.NOMBRE
import com.bornidea.re_circulapp.model.utils.Constants.USER
import com.bornidea.re_circulapp.view.activities.EditProfileActivity
import com.bornidea.re_circulapp.view.activities.Login
import com.bornidea.re_circulapp.view.activities.Registro.Companion.METODO_EMAIL
import com.bornidea.re_circulapp.viewmodel.EditProfileViewModel
import com.bornidea.re_circulapp.viewmodel.EditProfileViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class ProfileFragment : Fragment() {

    private var preferences: SharedPreferences? = null
    private lateinit var editprofileViewModel: EditProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        /**Inicializar ViewModel*/
        val repository = EditProfileRepository()
        val factory = EditProfileViewModelFactory(repository)
        editprofileViewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]

        /**Cerrar sesión*/
        binding.btLogout.setOnClickListener {
            val preferences =
                requireActivity().getSharedPreferences(USER, AppCompatActivity.MODE_PRIVATE)
            val metodo = preferences.getString(METODO, "")
            if (metodo == METODO_EMAIL || metodo == "") {
                val editor = preferences.edit()
                editor.putBoolean("isActive", false)
                editor.apply()

                startActivity(
                    Intent(
                        requireContext(),
                        Login::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            } else {
                val editor = preferences.edit()
                editor.putBoolean("isActive", false)
                editor.apply()

                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
                googleSignInClient.signOut()

                startActivity(
                    Intent(
                        requireContext(),
                        Login::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }
        binding.btCompartir.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Descarga rEColapp: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }

        binding.btCuenta.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        preferences =
            requireActivity().getSharedPreferences("Login", AppCompatActivity.MODE_PRIVATE)
        getInfoUser(preferences?.getString("correo", "") ?: "")
    }
    private fun getInfoUser(correo: String) {
        val editProfileRequest = EditProfileRequest(correo)
        editprofileViewModel.getDataUser(editProfileRequest)
            .observe(viewLifecycleOwner, Observer { response ->
                binding.apply {
                    tvName.text = response.nombre
                }

            })
    }
}