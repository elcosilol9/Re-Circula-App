package com.bornidea.re_circulapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityRegistroDetalleBinding

class RegistroDetalle : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registro_detalle)
        supportActionBar?.hide()

    }
}