package com.bornidea.re_circulapp.view.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.FragmentLocationDialogBinding
import com.bornidea.re_circulapp.view.activities.MenuActivity

class LocationDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentLocationDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location_dialog, container, false)

        binding.btnAceptar.setOnClickListener {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MenuActivity.TAG_LOCATION
            )
            dismiss()
        }
        return binding.root
    }

}