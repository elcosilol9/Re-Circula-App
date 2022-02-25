package com.bornidea.re_circulapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bornidea.re_circulapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ReCirculapp)
        super.onCreate(savedInstanceState)
        /**DataBinding*/
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        /**Quitar ActionBar*/
        supportActionBar?.hide()




    }
}