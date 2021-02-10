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
import com.amshotzz.ezeeweather.utils.network.NetworkHelper
import com.amshotzz.ezeeweather.utils.rx.RxSchedulerProvider
import com.amshotzz.ezeeweather.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule() {

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
    fun provideNetworkService(@ApplicationContext context: Context): NetworkService =
        Networking.create(
            "",
            BuildConfig.BaseUrl,// 10MB
            context
        )
    @Singleton
    @Provides
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper = NetworkHelper(context)
    @Provides
    fun provideAlertDialogBuilder(@ApplicationContext context: Context): AlertDialog.Builder = AlertDialog.Builder(context)
    @Provides
    fun provideLayoutInflater(@ApplicationContext context: Context): LayoutInflater = LayoutInflater.from(context)
    @Provides
    fun provideEzeeWeatherDataRepository(@ApplicationContext context: Context): EzeeWeatherDataRepository? =
        EzeeWeatherDataRepository.getInstance(context)

}