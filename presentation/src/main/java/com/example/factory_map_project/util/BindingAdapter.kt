package com.example.factory_map_project.util

import android.content.Context
import android.graphics.Paint
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
@BindingAdapter("setSelectPicker")
fun setSelectPicker(picker: NumberPicker, list: List<String>){
    Timber.d("list : $list")
    picker.minValue = 0
    picker.maxValue = list.size-1
    picker.displayedValues = list.toTypedArray()
}

/**
 * 키보드 Search 액션 버튼 이벤트 처리
 */
@BindingAdapter("setEditTextActionButton")
fun setEditTextActionButton(editText: EditText, onClick: () -> Unit){
    editText.setOnEditorActionListener { v, actionId, event ->
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            // 키보드 내리기
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
            onClick()
            true
        }else{
            false
        }
    }
}








private fun addRecyclerViewDivider(recyclerView: RecyclerView){
    Timber.d("addRecyclerViewDivider 생성")
    val dividerItemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
    val dividerDrawable = ContextCompat.getDrawable(recyclerView.context, R.drawable.shape_divider)
    dividerItemDecoration.setDrawable(dividerDrawable!!)
    recyclerView.addItemDecoration(com.example.factory_map_project.util.adapter.DividerItemDecoration(recyclerView.context)
    )
}