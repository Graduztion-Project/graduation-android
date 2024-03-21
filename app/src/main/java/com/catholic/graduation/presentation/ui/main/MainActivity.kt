package com.catholic.graduation.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.catholic.graduation.R
import com.catholic.graduation.databinding.ActivityMainBinding
import com.catholic.graduation.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBnv()


    }


    private fun setBnv() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.mainBnv.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item,navController)
                navController.popBackStack(item.itemId, inclusive = false)
                true
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.map_fragment || destination.id == R.id.stats_fragment) {
                // todo bnv show 해야되는 frag
                binding.mainBnv.visibility = View.VISIBLE
            } else {
                binding.mainBnv.visibility = View.INVISIBLE
            }
        }
    }

}