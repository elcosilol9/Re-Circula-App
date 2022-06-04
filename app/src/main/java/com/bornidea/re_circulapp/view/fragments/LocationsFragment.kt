package com.bornidea.re_circulapp.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.FragmentLocationsBinding
import com.bornidea.re_circulapp.model.repository.NegociosRepository
import com.bornidea.re_circulapp.model.request.NegociosRequest
import com.bornidea.re_circulapp.model.response.Negocios
import com.bornidea.re_circulapp.view.fragments.FichaTecnicaDialogFragment.Companion.NEGOCIO
import com.bornidea.re_circulapp.view.fragments.FichaTecnicaDialogFragment.Companion.TAG_NEGOCIO
import com.bornidea.re_circulapp.viewmodel.NegociosViewModel
import com.bornidea.re_circulapp.viewmodel.NegociosViewModelFactory
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson


class LocationsFragment : Fragment(), OnMapReadyCallback {

    private var imMarcador: Int? = null
    private lateinit var mMap: GoogleMap
    private lateinit var negociosViewModel: NegociosViewModel
    private lateinit var binding: FragmentLocationsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_locations, container, false)

        /**Inicializar viewModel*/
        val repository = NegociosRepository()
        val factory = NegociosViewModelFactory(repository)
        negociosViewModel = ViewModelProvider(this, factory)[NegociosViewModel::class.java]
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.btBuscarServicio.setOnClickListener {
            if (verifyContent()) {
                mMap.clear()
                binding.containerProgress.visibility = View.VISIBLE
                initSearch()
            }
        }

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun initlLocationPermission() {
        if (!::mMap.isInitialized) return
        if (isLocationPermission()) {
            mMap.isMyLocationEnabled = true

        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            val dialogSettings = SettingsLocationDialogFragment()
            dialogSettings.isCancelable = false
            dialogSettings.show(
                requireActivity().supportFragmentManager,
                "TAG_LOCATION_SETTINGS"
            )
        } else {
            val dialog = LocationDialogFragment()
            dialog.isCancelable = false
            dialog.show(requireActivity().supportFragmentManager, "TAG_LOCATIONFRAGMENT")
        }
    }

    private fun isLocationPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireActivity().applicationContext,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun verifyContent(): Boolean {
        var contador = 0
        binding.apply {
            textInputMunicipio.error = null
            textInputProducto.error = null
            textInputServicio.error = null
            if (autoCompleteMunicipio.text.isNotBlank() && autoCompleteMunicipio.text.isNotEmpty()) {
                contador++
            } else {
                textInputMunicipio.error = "Seleccione un Municipio"
            }
            if (autoCompleteProducto.text.isNotBlank() && autoCompleteProducto.text.isNotBlank()) {
                contador++
            } else {
                textInputProducto.error = "Seleccione un Producto"
            }
            if (autoCompleteServicio.text.isNotBlank() && autoCompleteServicio.text.isNotEmpty()) {
                contador++
            } else {
                textInputServicio.error = "Seleccione un Servicio"
            }
        }
        return contador == 3
    }

    private fun initSearch() {
        binding.apply {
            val negociosRequest = NegociosRequest(
                producto = getProductoClave(),
                servicio = getServicioClave(),
                localidad = autoCompleteMunicipio.text.toString()
            )
            imMarcador = getIconServicio()
            negociosViewModel.requestNegocios(negociosRequest)
                .observe(viewLifecycleOwner, Observer { response ->
                    binding.containerProgress.visibility = View.GONE
                    when (response.codigoRespuesta) {
                        200 -> {
                            //TODO BORRAR BUSQUEDAS
                            //TODO editar datos de la cuenta
                            //TODO Google
                            setMarcadores(response.negocios)
                        }
                        else -> {
                            Toast.makeText(
                                requireContext(),
                                "Hubo un error al conectar con el servidor",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
    }

    private fun setMarcadores(negocios: List<Negocios>) {
        negocios.forEach { negocio ->
            run {
                val coordinates =
                    LatLng(negocio.latitud.toDouble(), negocio.longitud.toDouble())
                val jsonTitle = Gson().toJson(negocio)
                val marker =
                    MarkerOptions().position(coordinates).title(jsonTitle)
                        .icon(
                            BitmapDescriptorFactory.fromResource(imMarcador!!)
                        )
                mMap.addMarker(marker)
            }
        }
    }

    private fun getIconServicio(): Int {
        return when (binding.autoCompleteServicio.text.toString()) {
            resources.getStringArray(R.array.servicios)[0] -> R.drawable.ic_reparacion
            resources.getStringArray(R.array.servicios)[1] -> R.drawable.ic_mantenimiento
            resources.getStringArray(R.array.servicios)[2] -> R.drawable.ic_hojalateria_pintura
            resources.getStringArray(R.array.servicios)[3] -> R.drawable.ic_lavado_lubricado
            resources.getStringArray(R.array.servicios)[4] -> R.drawable.ic_tapiceria
            resources.getStringArray(R.array.servicios)[5] -> R.drawable.ic_carroceria_cristales
            resources.getStringArray(R.array.servicios)[6] -> R.drawable.ic_alineaci_n_balanceo
            resources.getStringArray(R.array.servicios)[7] -> R.drawable.ic_autopartes
            resources.getStringArray(R.array.servicios)[8] -> R.drawable.ic_blindaje
            else -> 0
        }
    }

    private fun getServicioClave(): String {
        return when (binding.autoCompleteServicio.text.toString()) {
            resources.getStringArray(R.array.servicios)[0] -> "reparacion"
            resources.getStringArray(R.array.servicios)[1] -> "mantenimiento"
            resources.getStringArray(R.array.servicios)[2] -> "hojalateria"
            resources.getStringArray(R.array.servicios)[3] -> "lavado"
            resources.getStringArray(R.array.servicios)[4] -> "tapiceria"
            resources.getStringArray(R.array.servicios)[5] -> "instalacion"
            resources.getStringArray(R.array.servicios)[6] -> "alineacion"
            resources.getStringArray(R.array.servicios)[7] -> "autopartes"
            resources.getStringArray(R.array.servicios)[8] -> "servicio"
            else -> ""
        }
    }

    private fun getProductoClave(): String {
        return when (binding.autoCompleteProducto.text.toString()) {
            resources.getStringArray(R.array.productos)[0] -> "piel"
            resources.getStringArray(R.array.productos)[1] -> "motocicletas"
            resources.getStringArray(R.array.productos)[2] -> "hogar"
            resources.getStringArray(R.array.productos)[3] -> "electronico"
            resources.getStringArray(R.array.productos)[4] -> "hogar"
            resources.getStringArray(R.array.productos)[5] -> "automoviles"
            resources.getStringArray(R.array.productos)[6] -> "llantas"
            resources.getStringArray(R.array.productos)[7] -> "bicicletas"
            resources.getStringArray(R.array.productos)[8] -> "comercial"
            resources.getStringArray(R.array.productos)[9] -> "materiales"
            resources.getStringArray(R.array.productos)[10] -> "forestal"
            else -> ""
        }
    }

    override fun onResume() {
        super.onResume()
        setArrayProductos()
        setArrayServicios()
        setArrayMunicipios()
    }

    private fun setArrayMunicipios() {
        val municipios = resources.getStringArray(R.array.municipios)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, municipios)
        binding.autoCompleteMunicipio.setAdapter(arrayAdapter)
    }

    private fun setArrayServicios() {
        val servicios = resources.getStringArray(R.array.servicios)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, servicios)
        binding.autoCompleteServicio.setAdapter(arrayAdapter)
    }

    private fun setArrayProductos() {
        val productos = resources.getStringArray(R.array.productos)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, productos)
        binding.autoCompleteProducto.setAdapter(arrayAdapter)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initlLocationPermission()
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                val arguments = Bundle()
                arguments.putString(NEGOCIO, marker.title)

                val dialogInfo = FichaTecnicaDialogFragment()
                dialogInfo.isCancelable = true
                dialogInfo.arguments = arguments
                dialogInfo.show(requireActivity().supportFragmentManager, TAG_NEGOCIO)
            }
            true
        }
    }

}