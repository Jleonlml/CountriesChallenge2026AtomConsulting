package com.devart.countrieschallange2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.devart.countrieschallange2026.presentation.navigation.CountriesNavHost
import com.devart.countrieschallange2026.presentation.theme.CountriesChallange2026Theme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CountriesChallange2026Theme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        CountriesNavHost(
                            navController = navController,
                            viewModelFactory = viewModelFactory
                        )
                    }
                }
            }
        }
    }
}