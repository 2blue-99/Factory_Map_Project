package com.example.factory_map_project.util.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.ItemCompareBinding
import com.example.factory_map_project.databinding.ItemSpinnerBinding

class CompareAdapter(
    private val list: List<FactoryInfo>,
    private val onSelect: (Int) -> Unit
): RecyclerView.Adapter<CompareAdapter.BindViewHolder>() {

    inner class BindViewHolder(private val binding: ItemCompareBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.root.setOnClickListener {
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.dark_gray))
            }
            binding.data = list[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCompareBinding.inflate(inflater, parent, false)
        return BindViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size
}