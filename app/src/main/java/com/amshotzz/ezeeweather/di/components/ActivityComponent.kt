package com.amshotzz.ezeeweather.di.components

import com.amshotzz.ezeeweather.di.ActivityScope
import com.amshotzz.ezeeweather.di.modules.ActivityModule
import com.amshotzz.ezeeweather.main.MainActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent{
    fun inject(mainActivity: MainActivity)
}