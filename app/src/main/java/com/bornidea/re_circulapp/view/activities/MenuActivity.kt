package com.bornidea.re_circulapp.view.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bornidea.re_circulapp.R
import com.bornidea.re_circulapp.databinding.ActivityMenuBinding
import com.bornidea.re_circulapp.view.fragments.SettingsLocationDialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivity : AppCompatActivity() {

    companion object {
        var TAG_LOCATION = 0
    }

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)
        supportActionBar?.hide()

        /**Configuracion del menu*/
        val navigationView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationFragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigationLocation, R.id.navigationProfile))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            TAG_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navController.navigate(R.id.navigationLocation)
                } else {
                    val dialogSettings = SettingsLocationDialogFragment()
                    dialogSettings.isCancelable = false
                    dialogSettings.show(
                        supportFragmentManager,
                        "TAG_LOCATION_SETTINGS"
                    )
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}