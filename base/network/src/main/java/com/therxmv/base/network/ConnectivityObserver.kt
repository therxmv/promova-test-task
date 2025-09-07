package com.therxmv.base.network

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
class ConnectivityObserver(
    private val connectivityManager: ConnectivityManager,
) {

    private val _isConnectedState = MutableStateFlow(false)
    val isConnectedFlow = _isConnectedState.asStateFlow()

    init {
        registerCallback()
    }

    private fun registerCallback() {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                _isConnectedState.update { false }
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, capabilities)
                val validatedConnection = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                _isConnectedState.update { validatedConnection }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
    }
}