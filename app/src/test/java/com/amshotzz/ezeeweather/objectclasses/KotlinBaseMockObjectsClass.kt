package com.amshotzz.ezeeweather.objectclasses

import com.amshotzz.ezeeweather.api.network.NetworkService
import com.amshotzz.ezeeweather.application.EzeeWeatherApplication
import com.amshotzz.ezeeweather.database.EzeeWeatherDataRepository
import com.amshotzz.ezeeweather.database.dao.EzeeWeatherDao
import com.amshotzz.ezeeweather.utils.TestSchedulerProvider
import com.amshotzz.ezeeweather.utils.mock
import com.amshotzz.ezeeweather.utils.network.NetworkHelper
import com.amshotzz.ezeeweather.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

open class KotlinBaseMockObjectsClass {
    val mockSchedulerProvider = mock<SchedulerProvider>()
    val testSchedulerProvider = TestSchedulerProvider()
    val mockCompositeDisposable = mock<CompositeDisposable>()
    val mockNetworkService = mock<NetworkService>()
    val mockApplication = mock<EzeeWeatherApplication>()
    val mockNetworkHelper = mock<NetworkHelper>()
    val mockDataRepository = mock<EzeeWeatherDataRepository>()
    val mockDao = mock<EzeeWeatherDao>()
}