package com.amshotzz.ezeeweather.mainactivitytest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.amshotzz.ezeeweather.BuildConfig
import com.amshotzz.ezeeweather.database.entity.WeatherEntity
import com.amshotzz.ezeeweather.main.MainActivityRepository
import com.amshotzz.ezeeweather.main.MainActivityViewModel
import com.amshotzz.ezeeweather.main.dataClass.WeatherDataClass
import com.amshotzz.ezeeweather.objectclasses.KotlinBaseMockObjectsClass
import com.amshotzz.ezeeweather.utils.common.Resource
import com.amshotzz.ezeeweather.utils.mock
import com.amshotzz.ezeeweather.utils.whenever
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class MainActivityViewModelTest : KotlinBaseMockObjectsClass() {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val mockViewModel = mock<MainActivityViewModel>()
    private val mockMainActivityRepositoryTest = mock<MainActivityRepository>()
    private val messageStringObserver = mock<Observer<Resource<String>>>()
    private val weatherLiveDataObserver = mock<Observer<WeatherEntity>>()
    private val temperatureObserver = mock<Observer<CharSequence>>()
    private val temperatureDescObserver = mock<Observer<CharSequence>>()
    private val sunriseObserver = mock<Observer<CharSequence>>()
    private val sunsetObserver = mock<Observer<CharSequence>>()
    private val pressureObserver = mock<Observer<CharSequence>>()
    private val humidityObserver = mock<Observer<CharSequence>>()
    private val windSpeedObserver = mock<Observer<CharSequence>>()
    private val windDegreeObserver = mock<Observer<CharSequence>>()
    private val generateJobIdLiveDataObserver = mock<Observer<WeatherDataClass>>()
    private val showDialogObserver = mock<Observer<Boolean>>()
    private val viewModel by lazy {
        MainActivityViewModel(
            testSchedulerProvider,
            mockCompositeDisposable,
            mockNetworkHelper,
            mockMainActivityRepositoryTest
        )
    }

    @Before
    fun initTest() {
        Mockito.reset(
            mockCompositeDisposable,
            mockNetworkHelper,
            mockMainActivityRepositoryTest,
            messageStringObserver,
            generateJobIdLiveDataObserver,
            windDegreeObserver,
            windSpeedObserver,
            humidityObserver,
            pressureObserver,
            sunsetObserver,
            sunriseObserver,
            temperatureDescObserver,
            temperatureObserver,
            weatherLiveDataObserver,
            showDialogObserver
        )
    }

    @Test
    fun verifyGetWeatherEntityData() {
        val weatherDataClass = WeatherDataClass()
        val weatherEntity = WeatherEntity(
            -1,
            weatherDataClass.name ?: "",
            weatherDataClass.weather?.get(0)?.icon ?: "",
            weatherDataClass.weather?.get(0)?.main ?: "",
            weatherDataClass.main?.temp ?: 0.0,
            weatherDataClass.main?.tempMin ?: 0.0,
            weatherDataClass.main?.humidity ?: 0,
            weatherDataClass.main?.pressure ?: 0,
            weatherDataClass.main?.tempMax ?: 0.0,
            weatherDataClass.sys?.sunrise ?: 0,
            weatherDataClass.sys?.sunset ?: 0,
            weatherDataClass.wind?.deg ?: 0,
            weatherDataClass.wind?.speed ?: 0.0
        )
        val mWeatherEntity = viewModel.getWeatherEntityData(weatherDataClass)
        assertEquals(weatherEntity, mWeatherEntity)
    }

    @Test
    fun verifySetLiveData() {
        viewModel.temperature.observeForever(temperatureObserver)
        viewModel.temperatureDesc.observeForever(temperatureDescObserver)
        viewModel.sunrise.observeForever(sunriseObserver)
        viewModel.sunset.observeForever(sunsetObserver)
        viewModel.pressure.observeForever(pressureObserver)
        viewModel.humidity.observeForever(humidityObserver)
        viewModel.windSpeed.observeForever(windSpeedObserver)
        viewModel.windDegree.observeForever(windDegreeObserver)
        viewModel.setLiveData(weatherEntity)
        val argumentCaptorCharSequence = ArgumentCaptor.forClass(CharSequence::class.java)
        argumentCaptorCharSequence.run {
            Mockito.verify(temperatureObserver, Mockito.times(1)).onChanged(capture())
            Mockito.verify(temperatureDescObserver, Mockito.times(1)).onChanged(capture())
            Mockito.verify(sunriseObserver, Mockito.times(1))
                .onChanged(ArgumentMatchers.anyString())
            Mockito.verify(sunsetObserver, Mockito.times(1)).onChanged(ArgumentMatchers.anyString())
            Mockito.verify(pressureObserver, Mockito.times(1))
                .onChanged(ArgumentMatchers.anyString())
            Mockito.verify(humidityObserver, Mockito.times(1))
                .onChanged(ArgumentMatchers.anyString())
            Mockito.verify(windSpeedObserver, Mockito.times(1))
                .onChanged(ArgumentMatchers.anyString())
            Mockito.verify(windDegreeObserver, Mockito.times(1)).onChanged(capture())
        }

    }

    @Test
    fun verifyInsertAndSetWeatherData() {
        viewModel.weatherLiveData.observeForever(weatherLiveDataObserver)
        viewModel.insertAndSetWeatherData(WeatherDataClass())
        val argumentCaptorWeather = ArgumentCaptor.forClass(WeatherEntity::class.java)
        argumentCaptorWeather.run {
            Mockito.verify(weatherLiveDataObserver, Mockito.times(1)).onChanged(capture())
        }
    }

    @Test
    fun verifyGetWeatherByCityName() {
        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.getWeatherByCityName("Pune", BuildConfig.OpenWheatherKey))
            .thenReturn(Single.just(WeatherDataClass()))
        whenever(mockMainActivityRepositoryTest.getWeatherByCityName(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(WeatherDataClass()))
        viewModel.showDialog.observeForever(showDialogObserver)
        viewModel.generateJobIdLiveData.observeForever(generateJobIdLiveDataObserver)
        viewModel.getWeatherByCityName("Pune")
        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showDialogObserver, Mockito.times(2)).onChanged(capture())
            Mockito.verify(generateJobIdLiveDataObserver, Mockito.times(1))
                .onChanged(WeatherDataClass())
        }
    }

    @Test
    fun verifyGetWeatherByCityNameError() {
        whenever(mockNetworkHelper.isNetworkConnected()).thenReturn(true)
        whenever(mockNetworkService.getWeatherByCityName("Pune", BuildConfig.OpenWheatherKey))
            .thenReturn(Single.error(getErrorResponse()))
        whenever(mockMainActivityRepositoryTest.getWeatherByCityName(ArgumentMatchers.anyString()))
            .thenReturn(Single.error(getErrorResponse()))
        viewModel.showDialog.observeForever(showDialogObserver)
        viewModel.messageString.observeForever(messageStringObserver)
        viewModel.getWeatherByCityName("Pune")
        val argumentCaptorShowDialog = ArgumentCaptor.forClass(Boolean::class.java)
        argumentCaptorShowDialog.run {
            Mockito.verify(showDialogObserver, Mockito.times(2)).onChanged(capture())
            Mockito.verify(messageStringObserver, Mockito.times(1)).onChanged(
                Resource.error(
                    errorMessage
                )
            )
        }
    }


}