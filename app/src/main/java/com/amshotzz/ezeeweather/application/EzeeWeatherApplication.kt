package com.amshotzz.ezeeweather.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.amshotzz.ezeeweather.utils.common.LoadingDialog
import com.amshotzz.ezeeweather.utils.network.InternetAvailabilityChecker
import com.amshotzz.ezeeweather.utils.network.InternetConnectivityListener
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class EzeeWeatherApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks,
    InternetConnectivityListener {

    private var isConnected: Boolean = false
    private var mInternetAvailabilityChecker: InternetAvailabilityChecker? = null


    override fun onCreate() {
        super.onCreate()
        InternetAvailabilityChecker.init(this)
        /*Register Activity Lifecycle callbacks*/
        registerActivityLifecycleCallbacks(this)
    }


    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        this.isConnected = isConnected
    }

    override fun onActivityPaused(activity: Activity) {
        mInternetAvailabilityChecker?.removeInternetConnectivityChangeListener(this)
        LoadingDialog.unbind()
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        mInternetAvailabilityChecker = InternetAvailabilityChecker.instance
        mInternetAvailabilityChecker?.addInternetConnectivityListener(this)
        LoadingDialog.bindWith(activity)
    }

}