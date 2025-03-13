package com.example.factory_map_project.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.factory_map_project.databinding.ItemIndicatorBinding
import timber.log.Timber

class IndicatorAdapter(
    val array: Array<Boolean>,
): RecyclerView.Adapter<IndicatorAdapter.BindViewHolder>() {

    inner class BindViewHolder(private val binding: ItemIndicatorBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                Timber.d("isChecked : $isChecked")
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

    override fun getItemCount(): Int = array.size
}