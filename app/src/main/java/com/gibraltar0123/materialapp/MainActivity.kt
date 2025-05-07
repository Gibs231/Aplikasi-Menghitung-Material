package com.gibraltar0123.materialapp.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.gibraltar0123.materialapp.ui.theme.MaterialAppTheme
import com.gibraltar0123.materialapp.util.MaterialViewModelFactory
import com.gibraltar0123.materialapp.database.MaterialDb
import com.gibraltar0123.materialapp.navigation.SetupNavGraph
import com.gibraltar0123.materialapp.viewmodel.MaterialViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Membuat instance MaterialViewModelFactory
        val factory = MaterialViewModelFactory(applicationContext)
        val viewModel = ViewModelProvider(this, factory).get(MaterialViewModel::class.java)

        setContent {
            MaterialAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}
