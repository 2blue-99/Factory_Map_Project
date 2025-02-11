package com.example.factory_map_project.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.example.factory_map_project.BuildConfig
import com.example.factory_map_project.util.event.AppEvent
import timber.log.Timber

class DialogUtil(
    private val context: Context
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun showDialog(
        title: String,
        content:String,
        event: AppEvent.ShowPopup? = null,
        negativeCallBack: () -> Unit = {},
        positiveCallBack: () -> Unit = {}
    ){
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("확인"){ dialog, which ->
                event?.tryEmit(true)
                positiveCallBack()
            }
            .setNegativeButton("취소"){ dialog, which ->
                event?.cancel()
                negativeCallBack()
            }
            .setOnDismissListener {
                event?.cancel()
            }
            .show()
    }

    fun showMessageDialog(event: AppEvent.ShowPopup){
        showDialog(event.content.title, event.content.content, event)
    }

    fun showCallBackDialog(
        popup: PopupContent,
        negativeCallBack: () -> Unit,
        positiveCallBack: () -> Unit,
        vararg args: String
    ){
        val dialogContent = String.format(popup.content, *args)
        showDialog(popup.title, dialogContent, null, negativeCallBack, positiveCallBack)
    }
}