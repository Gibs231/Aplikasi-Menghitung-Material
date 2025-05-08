package com.gibraltar0123.materialapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.gibraltar0123.materialapp.navigation.SetupNavGraph
import com.gibraltar0123.materialapp.ui.theme.defaults.DefaultsTheme
import com.gibraltar0123.materialapp.ui.theme.yellow.YellowTheme

import com.gibraltar0123.materialapp.util.SettingDataStore
import com.gibraltar0123.materialapp.util.SettingViewModel
import com.gibraltar0123.materialapp.util.SettingViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var settingDataStore: SettingDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingDataStore = SettingDataStore(this)

        setContent {
            val viewModel: SettingViewModel = viewModel(
                factory = SettingViewModelFactory(settingDataStore)
            )


            val isYellowTheme by viewModel.isYellowTheme.collectAsState()

            if (isYellowTheme) {

                YellowTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        SetupNavGraph(navController = navController)
                    }
                }
            } else {

                DefaultsTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        SetupNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
