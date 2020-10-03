package com.amshotzz.ezeeweather.di.modules

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.amshotzz.ezeeweather.BuildConfig
import com.amshotzz.ezeeweather.api.network.NetworkService
import com.amshotzz.ezeeweather.api.network.Networking
import com.amshotzz.ezeeweather.application.EzeeWeatherApplication
import com.amshotzz.ezeeweather.database.EzeeWeatherDataRepository
import com.amshotzz.ezeeweather.di.ApplicationContext
import com.amshotzz.ezeeweather.utils.network.NetworkHelper
import com.amshotzz.ezeeweather.utils.rx.RxSchedulerProvider
import com.amshotzz.ezeeweather.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: EzeeWeatherApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    /**
     * Since this function do not have @Singleton then each time CompositeDisposable is injected
     * then a new instance of CompositeDisposable will be provided
     */

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

    /**
     * We need to write @Singleton on the provide method if we are create the instance inside this method
     * to make it singleton. Even if we have written @Singleton on the instance's class
     */
    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService =
        Networking.create(
            "",
            BuildConfig.BaseUrl,
            application.cacheDir,
            10 * 1024 * 1024, // 10MB
            application
        )

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = NetworkHelper(application)

    @Provides
    fun provideAlertDialogBuilder(): AlertDialog.Builder = AlertDialog.Builder(application)

    @Provides
    fun provideLayoutInflater(): LayoutInflater = LayoutInflater.from(application)

    @Provides
    fun provideEzeeWeatherDataRepository(): EzeeWeatherDataRepository? =
        EzeeWeatherDataRepository.getInstance(application)

}