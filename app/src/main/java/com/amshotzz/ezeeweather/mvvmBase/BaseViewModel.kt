package com.amshotzz.ezeeweather.mvvmBase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amshotzz.ezeeweather.R
import com.amshotzz.ezeeweather.utils.common.Resource
import com.amshotzz.ezeeweather.utils.network.NetworkHelper
import com.amshotzz.ezeeweather.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.net.ssl.HttpsURLConnection

abstract class BaseViewModel(
    protected val schedulerProvider: SchedulerProvider,
    protected val compositeDisposable: CompositeDisposable,
    protected val NetworkHelper: NetworkHelper
) : ViewModel() {

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    val messageStringId: MutableLiveData<Resource<Int>> = MutableLiveData()
    val messageString: MutableLiveData<Resource<String>> = MutableLiveData()
    val showDialog: MutableLiveData<Boolean> = MutableLiveData()

    protected fun checkInternetConnectionWithMessage(isShowDialog: Boolean = true): Boolean =
        if (NetworkHelper.isNetworkConnected()) {
            showDialog.postValue(isShowDialog)
            true
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            showDialog.postValue(false)
            false
        }

    protected fun checkInternetConnection(): Boolean = NetworkHelper.isNetworkConnected()

    protected fun handleNetworkError(err: Throwable?) =
        err?.let {
            NetworkHelper.castToNetworkError(it).run {
                when (status) {
                    -1 -> messageStringId.postValue(Resource.error(R.string.network_default_error))
                    0 -> messageStringId.postValue(Resource.error(R.string.server_connection_error))
                    HttpsURLConnection.HTTP_UNAUTHORIZED -> {
                        forcedLogoutUser()
                        messageStringId.postValue(Resource.error(R.string.permission_denied))
                    }
                    HttpsURLConnection.HTTP_INTERNAL_ERROR ->
                        messageStringId.postValue(Resource.error(R.string.network_internal_error))
                    HttpsURLConnection.HTTP_UNAVAILABLE ->
                        messageStringId.postValue(Resource.error(R.string.network_server_not_available))
                    else -> messageString.postValue(Resource.error(message))
                }
            }
        }

    protected open fun forcedLogoutUser() {
        // do something
    }

    abstract fun onCreate()
}