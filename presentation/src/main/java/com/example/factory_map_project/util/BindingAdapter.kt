package com.example.factory_map_project.util

import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.factory_map_project.R

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
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        val dividerDrawable = ContextCompat.getDrawable(recyclerView.context, R.drawable.shape_divider)
        dividerItemDecoration.setDrawable(dividerDrawable!!)
        recyclerView.addItemDecoration(com.example.factory_map_project.util.adapter.DividerItemDecoration(recyclerView.context)
        )
    }
}

/**
 *
 */
@BindingAdapter("setCheckBackground")
fun setCheckBackground(view: ImageView, state: Int){
    view.setImageResource(
        when(state){
            STATE_UNKNOWN -> R.drawable.icon_unknown_marker
            STATE_SUCCESS -> R.drawable.icon_success_marker
            else -> R.drawable.icon_fail_marker
        }
    )

}