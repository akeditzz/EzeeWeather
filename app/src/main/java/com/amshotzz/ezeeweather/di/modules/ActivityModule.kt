package com.amshotzz.ezeeweather.di.modules

import androidx.lifecycle.ViewModelProviders
import com.amshotzz.ezeeweather.main.MainActivityRepository
import com.amshotzz.ezeeweather.main.MainActivityViewModel
import com.amshotzz.ezeeweather.mvvmBase.BaseActivity
import com.amshotzz.ezeeweather.mvvmBase.ViewModelProviderFactory
import com.amshotzz.ezeeweather.utils.network.NetworkHelper
import com.amshotzz.ezeeweather.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule(private val activity: BaseActivity<*>){
    @Provides
    fun provideMainActivityViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, mainActivityRepository: MainActivityRepository
    ): MainActivityViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(MainActivityViewModel::class) {
            MainActivityViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                mainActivityRepository
            )
            //this lambda creates and return MainActivityViewModel
        }).get(MainActivityViewModel::class.java)
}