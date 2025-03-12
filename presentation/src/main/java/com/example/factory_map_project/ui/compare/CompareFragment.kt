package com.example.factory_map_project.ui.compare

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.databinding.FragmentCompareBinding
import com.example.factory_map_project.ui.base.BaseFragment
import com.example.factory_map_project.util.ARG_CONTENT
import com.example.factory_map_project.util.ARG_SECOND_CONTENT
import com.example.factory_map_project.util.CommonUtil.getData
import com.example.factory_map_project.util.adapter.CompareAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CompareFragment : BaseFragment<FragmentCompareBinding, CompareViewModel>(
    FragmentCompareBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: CompareViewModel by viewModels()


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {
        viewModel.remoteList.value = arguments?.getData<Array<FactoryInfo>>(ARG_CONTENT)?.toList() ?: emptyList()
        viewModel.localList.value = arguments?.getData<Array<FactoryInfo>>(ARG_SECOND_CONTENT)?.toList() ?: emptyList()
    }

    override fun setUI() {
        // RecyclerView 세팅
        setRecyclerView(binding.remoteList, viewModel.remoteList.value, {})
        setRecyclerView(binding.localeList, viewModel.localList.value, {})
//        setRecyclerView(binding.checkList, viewModel.remoteList.value, {})
    }

    override fun setObserver() {}


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun setRecyclerView(recyclerView: RecyclerView, list: List<FactoryInfo>, onSelect: (Int) -> Unit) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CompareAdapter(
            list = list,
            onSelect = {
                onSelect(it)
            }
        )
    }
}