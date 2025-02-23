package com.example.factory_map_project.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.factory_map_project.R
import com.example.factory_map_project.util.event.AppEvent

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
        AlertDialog.Builder(context, R.style.CustomAlertDialog)
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

    fun showMessageDialog(popup: PopupContent, positiveCallBack: () -> Unit){
        showDialog(popup.title, popup.content, null, {}, positiveCallBack)
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