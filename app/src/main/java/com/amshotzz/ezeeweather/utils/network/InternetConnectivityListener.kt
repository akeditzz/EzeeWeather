package com.amshotzz.ezeeweather.utils.network

interface InternetConnectivityListener {

    fun onInternetConnectivityChanged(isConnected: Boolean)
}