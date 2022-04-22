package com.example.cdc

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cdc.databinding.ActivityCommonUserBinding

class CommonUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommonUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommonUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_common_user)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_search_info, R.id.navigation_information_report
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //从login activity处获取了额外参数 account
        val extraData = intent.getStringExtra("account")
        Log.e("MainActivity",extraData.toString())
        val bundle = bundleOf("account" to extraData.toString())
        navController.navigate(R.id.navigation_search_info, bundle)
    }
}