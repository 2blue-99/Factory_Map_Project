package com.example.factory_map_project.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.ItemCompareBinding

class CompareAdapter(
    private val list: List<Pair<FactoryInfo, FactoryInfo>>,
    private val onSelect: (Int, FactoryInfo) -> Unit
): RecyclerView.Adapter<CompareAdapter.BindViewHolder>() {

    inner class BindViewHolder(private val binding: ItemCompareBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.serverLayout.setOnClickListener {
                binding.serverLayout.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.more_light_gray))
                binding.localLayout.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
                onSelect(position, list[position].first)
            }

            binding.localLayout.setOnClickListener {
                binding.localLayout.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.more_light_gray))
                binding.serverLayout.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
                onSelect(position, list[position].second)
            }

            binding.serverData = list[position].first
            binding.localData = list[position].second
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