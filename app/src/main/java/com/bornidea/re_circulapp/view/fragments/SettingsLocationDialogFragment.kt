package com.bornidea.re_circulapp.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.FragmentSettingsLocationDialogBinding

class SettingsLocationDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentSettingsLocationDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings_location_dialog,
                container,
                false
            )

        binding.btnAceptar.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
            dismiss()
            requireActivity().finish()
        }
        return binding.root
    }

}