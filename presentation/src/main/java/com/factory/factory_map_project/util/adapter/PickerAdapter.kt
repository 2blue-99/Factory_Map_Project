package com.factory.factory_map_project.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.factory.domain.model.FilterData
import com.factory.factory_map_project.databinding.ItemFilterBinding

class PickerAdapter(
    val onDeleteTarget: (Int) -> Unit
): RecyclerView.Adapter<PickerAdapter.BindViewHolder>() {

    var inputList: List<FilterData> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class BindViewHolder(private val binding: ItemFilterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.root.setOnLongClickListener {
                onDeleteTarget(inputList[position].id)
                true
            }
            binding.target.text = inputList[position].target
            binding.keyword.text = inputList[position].keyword
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFilterBinding.inflate(inflater, parent, false)
        return BindViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = inputList.size
}