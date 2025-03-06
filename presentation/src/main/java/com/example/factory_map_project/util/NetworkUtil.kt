package com.example.factory_map_project.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NetworkUtil @Inject constructor(
    private val context: Context,
    private val updateState: (Boolean) -> Unit
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Timber.d("onAvailable : ${network} ")
            checkNetworkState()
        }

        /**
         *  끊긴 여부 파악
         *
         *  현재 연결 여부를 파악하지 않고, 끊긴 연결만 파악하기 때문에
         *  현재 연결 상태 체크 필요
         */
        override fun onLost(network: Network) {
            super.onLost(network)
            Timber.d("onLost : $network ")
            checkNetworkState()
        }
    }


    //**********************************************************************************************
    // Mark: Initialization
    //**********************************************************************************************
    init {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
        checkNetworkState()
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    /**
     *  현재 네트워크 상태 체크
     *
     *  delay 걸어야 정상적으로 반영된 값을 반환
     */
    private fun checkNetworkState() {
        CoroutineScope(Dispatchers.Main).launch {
            Timber.d("checkNetworkState : ${connectivityManager.activeNetwork}")
            delay(500)
            val currentNetwork = connectivityManager.activeNetwork != null
            updateState(currentNetwork)
        }
    }
}