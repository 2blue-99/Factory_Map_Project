package com.example.factory_map_project.util

import android.graphics.Paint
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.factory_map_project.R
import timber.log.Timber

@BindingAdapter("underline")
fun textUnderline(textView: TextView, boolean: Boolean){
    if(boolean){
        textView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * Recyclerview 아이템 구분선 추가
 */
@BindingAdapter("setDivider")
fun setRecyclerViewDivider(recyclerView: RecyclerView, divider: Boolean){
    if(divider){
        addRecyclerViewDivider(recyclerView)
    }
}

/**
 *
 */
//@BindingAdapter("setCheckBackground")
//fun setCheckBackground(view: ImageView, state: Int){
//    view.setImageResource(
//        when(state){
//            STATE_UNKNOWN -> R.drawable.icon_check_box
//            STATE_CHECK -> R.drawable.icon_check_box
//            else -> R.drawable.icon_fail_marker
//        }
//    )
//}

/**
 *
 */
@BindingAdapter("setSelectPicker")
fun setSelectPicker(picker: NumberPicker, list: List<String>){
    Timber.d("list : $list")
    picker.minValue = 0
    picker.maxValue = list.size-1
    picker.displayedValues = list.toTypedArray()
}

private fun addRecyclerViewDivider(recyclerView: RecyclerView){
    Timber.d("addRecyclerViewDivider 생성")
    val dividerItemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
    val dividerDrawable = ContextCompat.getDrawable(recyclerView.context, R.drawable.shape_divider)
    dividerItemDecoration.setDrawable(dividerDrawable!!)
    recyclerView.addItemDecoration(com.example.factory_map_project.util.adapter.DividerItemDecoration(recyclerView.context)
    )
}