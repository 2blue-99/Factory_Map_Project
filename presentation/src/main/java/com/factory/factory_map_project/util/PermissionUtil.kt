package com.factory.factory_map_project.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
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