package com.amshotzz.ezeeweather.utils.network

interface TaskFinished<T> {
    fun onTaskFinished(data: T)
}