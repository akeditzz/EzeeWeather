package com.amshotzz.ezeeweather.di.components

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.amshotzz.ezeeweather.api.network.NetworkService
import com.amshotzz.ezeeweather.application.EzeeWeatherApplication
import com.amshotzz.ezeeweather.database.EzeeWeatherDataRepository
import com.amshotzz.ezeeweather.di.ApplicationContext
import com.amshotzz.ezeeweather.di.modules.ApplicationModule
import com.amshotzz.ezeeweather.utils.network.NetworkHelper
import com.amshotzz.ezeeweather.utils.rx.SchedulerProvider
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: EzeeWeatherApplication)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    /**
     * These methods are written in ApplicationComponent because the instance of
     * NetworkService is singleton and is maintained in the ApplicationComponent's implementation by Dagger
     * For NetworkService singleton instance to be accessible to say DummyActivity's DummyViewModel
     * this ApplicationComponent must expose a method that returns NetworkService instance
     * This method will be called when NetworkService is injected in DummyViewModel.
     * Also, in ActivityComponent you can find dependencies = [ApplicationComponent::class] to link this relationship
     */
    fun getNetworkService(): NetworkService

    fun getNetworkHelper(): NetworkHelper

    fun getSchedulerProvider(): SchedulerProvider

    fun getCompositeDisposable(): CompositeDisposable

    fun getAlertDialogBuilder(): AlertDialog.Builder

    fun getLayoutInflater(): LayoutInflater

    fun getEzeeWeatherDataRepository():EzeeWeatherDataRepository?

}