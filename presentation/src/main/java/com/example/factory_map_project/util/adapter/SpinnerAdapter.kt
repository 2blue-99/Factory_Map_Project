package com.example.factory_map_project.util.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.factory_map_project.databinding.ItemSpinnerBinding

class SpinnerAdapter(
    private val list: List<String>,
    private val selectedPosition: Int,
    private val onSelect: (Int) -> Unit
): RecyclerView.Adapter<SpinnerAdapter.BindViewHolder>() {

    inner class BindViewHolder(private val binding: ItemSpinnerBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.root.setOnClickListener {
                onSelect(position)
            }
            binding.title.text = list[position]
            if(position == selectedPosition) binding.title.typeface = Typeface.DEFAULT_BOLD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSpinnerBinding.inflate(inflater, parent, false)
        return BindViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size
}