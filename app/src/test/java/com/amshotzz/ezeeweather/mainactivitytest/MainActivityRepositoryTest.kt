package com.amshotzz.ezeeweather.mainactivitytest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amshotzz.ezeeweather.BuildConfig
import com.amshotzz.ezeeweather.main.MainActivityRepository
import com.amshotzz.ezeeweather.main.dataClass.WeatherDataClass
import com.amshotzz.ezeeweather.objectclasses.KotlinBaseMockObjectsClass
import com.amshotzz.ezeeweather.utils.mock
import com.amshotzz.ezeeweather.utils.whenever
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class MainActivityRepositoryTest : KotlinBaseMockObjectsClass() {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val mockMainActivityRepositoryTest = mock<MainActivityRepository>()
    val mainActivityRepository by lazy {
        MainActivityRepository(mockNetworkService, mockDataRepository)
    }

    @Before
    fun initTest() {
        Mockito.reset(mockNetworkService, mockDataRepository, mockApplication)
    }

    @Test
    fun verifyConstructorParameters() {
        assertEquals(mockNetworkService, mainActivityRepository.networkService)
        assertEquals(mockDataRepository, mainActivityRepository.ezeeWeatherDataRepository)
    }

    @Test
    fun verifyGetWeatherByCityName() {
        whenever(mockNetworkService.getWeatherByCityName("Pune", BuildConfig.OpenWheatherKey))
            .thenReturn(Single.just(WeatherDataClass()))
        whenever(mockMainActivityRepositoryTest.getWeatherByCityName(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(WeatherDataClass()))
        mainActivityRepository.getWeatherByCityName("Pune").test().assertComplete()
    }


}