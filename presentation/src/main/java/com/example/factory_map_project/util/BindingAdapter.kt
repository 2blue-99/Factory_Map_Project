package com.example.factory_map_project.util

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("underline")
fun textUnderline(textView: TextView, boolean: Boolean){
    if(boolean){
        textView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}