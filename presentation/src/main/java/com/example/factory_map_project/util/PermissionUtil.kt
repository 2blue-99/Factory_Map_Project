package com.example.factory_map_project.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.example.factory_map_project.BuildConfig
import timber.log.Timber

class PermissionUtil(
    private val activity: Activity
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    val locationPermission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    } else{
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var permissionState = true
        for(permission in permissionArray){
            if(ActivityCompat.checkSelfPermission(activity.applicationContext, permission) != PackageManager.PERMISSION_GRANTED){
                Timber.d("what : $permission")
                permissionState = false
                break
            }
        }
        Timber.d("checkPermission : $permissionState")
        return permissionState
    }

    private fun checkRejectPermission(permissionArray: Array<String>): Boolean {
        var permissionState = true
        for(permission in permissionArray){
            if(activity.shouldShowRequestPermissionRationale(permission)){
                permissionState = false
                break
            }
        }
        Timber.d("checkRejectPermission : $permissionState")
        return permissionState
    }

    fun checkLocationPermission(): Boolean{
        return checkPermission(locationPermission)
    }

    fun checkLocationRejectPermission(): Boolean{
        return checkRejectPermission(locationPermission)
    }
}