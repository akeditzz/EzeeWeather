package com.amshotzz.ezeeweather.utils.permissions

interface ResponsePermissionCallback {
    fun onResult(permissionResult: List<String>)
}