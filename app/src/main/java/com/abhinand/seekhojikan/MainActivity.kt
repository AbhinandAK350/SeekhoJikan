package com.abhinand.seekhojikan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.abhinand.seekhojikan.core.navigation.AppNavigation
import com.abhinand.seekhojikan.core.theme.SeekhoJikanTheme
import com.abhinand.seekhojikan.core.utils.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeekhoJikanTheme {

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(connectivityObserver) {
                    var lastStatus: ConnectivityObserver.Status? = null
                    connectivityObserver.observe().collect { current ->
                        if (lastStatus == null) {
                            if (current != ConnectivityObserver.Status.Available) {
                                snackbarHostState.showSnackbar("No internet connection")
                            }
                        } else if (current != lastStatus) {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                when (current) {
                                    ConnectivityObserver.Status.Available -> "You are back online"
                                    ConnectivityObserver.Status.Losing -> "Connection is unstable"
                                    ConnectivityObserver.Status.Lost,
                                    ConnectivityObserver.Status.Unavailable -> "No internet connection"
                                }
                            )
                        }
                        lastStatus = current
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
