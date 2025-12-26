package com.abhinand.seekhojikan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.abhinand.seekhojikan.core.navigation.AppNavigation
import com.abhinand.seekhojikan.core.theme.SeekhoJikanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeekhoJikanTheme {
                AppNavigation()
            }
        }
    }
}