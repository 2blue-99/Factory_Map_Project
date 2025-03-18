package com.example.factory_map_project.util.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.ItemIndicatorBinding
import com.example.factory_map_project.util.SELECT_LOCAL
import com.example.factory_map_project.util.SELECT_SERVER

class IndicatorAdapter: RecyclerView.Adapter<IndicatorAdapter.BindViewHolder>() {

    var list: List<Int> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class BindViewHolder(private val binding: ItemIndicatorBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            // else 문에 색상을 넣을 경우 기존에 적용되어있는 Background 가 해제되기에 -1로 예외처리
            val color =
                if (list[position] == SELECT_SERVER) R.color.light_red
                else if (list[position] == SELECT_LOCAL) R.color.light_primary
                else -1
            if(color != -1){
                binding.indicatorBox.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, color)) // 버튼 색상을 파란색으로 변경
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIndicatorBinding.inflate(inflater, parent, false)
        return BindViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size
}