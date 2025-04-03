package com.gibraltar0123.materialapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gibraltar0123.materialapp.navigation.SetupNavGraph
import com.gibraltar0123.materialapp.ui.theme.MaterialAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialAppTheme {
                    SetupNavGraph()
                }
            }
        }
    }


