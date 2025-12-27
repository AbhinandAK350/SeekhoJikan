package com.abhinand.seekhojikan.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserver(
    context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> =
        callbackFlow {
            fun currentStatus(): ConnectivityObserver.Status {
                val network = connectivityManager.activeNetwork
                    ?: return ConnectivityObserver.Status.Unavailable

                val caps = connectivityManager.getNetworkCapabilities(network)
                    ?: return ConnectivityObserver.Status.Unavailable

                return if (
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                ) {
                    ConnectivityObserver.Status.Available
                } else {
                    ConnectivityObserver.Status.Unavailable
                }
            }

            trySend(currentStatus())

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(ConnectivityObserver.Status.Available)
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    trySend(ConnectivityObserver.Status.Losing)
                }

                override fun onLost(network: Network) {
                    trySend(ConnectivityObserver.Status.Lost)
                }

                override fun onUnavailable() {
                    trySend(ConnectivityObserver.Status.Unavailable)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
}

