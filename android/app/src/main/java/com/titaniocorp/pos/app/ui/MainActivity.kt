package com.titaniocorp.pos.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.*
import com.titaniocorp.pos.BuildConfig
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.activity.BaseActivity
import com.titaniocorp.pos.databinding.ActivityMainBinding
import com.titaniocorp.pos.util.Configurations
import com.titaniocorp.pos.util.Constants
import timber.log.Timber

/**
 * Actividad principal de la aplicación
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        navController = findNavController(this, R.id.nav_host_fragment)
        val topLevelDestinations = setOf(
            R.id.dashboardPOSFragment
        )

        appBarConfiguration = AppBarConfiguration
            .Builder(topLevelDestinations)
            .setDrawerLayout(binding.drawerLayout)
            .build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)

        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.text_app_version).text = BuildConfig.VERSION_NAME
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
    //endregion
}

