package com.bornidea.re_circulapp.view.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.FichaTecnicaDialogFragmentBinding
import com.bornidea.re_circulapp.model.response.Negocios
import com.google.gson.Gson


class FichaTecnicaDialogFragment : DialogFragment() {

    companion object {
        const val TAG_NEGOCIO = "TAG_NEGOCIO"
        const val NEGOCIO = "NEGOCIO"
    }

    private var negocio = Negocios()
    private lateinit var binding: FichaTecnicaDialogFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.ficha_tecnica_dialog_fragment,
                container,
                false
            )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        getargumentsOfMarker()

        initView()
        return binding.root
    }

    private fun initView() {
        binding.apply {
            tvTitle.text = negocio.nombre_unidad_economica
            tvClase.text = negocio.clase_de_actividad
            tvVialidad.text = negocio.nombre_vialidad
            tvNumExterior.text = negocio.Num_exterior_o_kilometro
            tvAsentamiento.text = negocio.nombre_asentamiento_humano
            tvCodigoPostal.text = negocio.codigo_postal

            if (negocio.telefono == "0")
                btTelefono.visibility = View.GONE
            btTelefono.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + negocio.telefono)
                startActivity(dialIntent)
            }
            if (negocio.email == "")
                btEmail.visibility = View.GONE
            btEmail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(negocio.email))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contacto desde Re-CirculApp")
                startActivity(intent)
            }
        }
    }

    private fun getargumentsOfMarker() {
        if (arguments != null) {
            val negocioString = arguments?.getString(NEGOCIO)
            negocio = Gson().fromJson(negocioString, Negocios::class.java)
        }
    }

}