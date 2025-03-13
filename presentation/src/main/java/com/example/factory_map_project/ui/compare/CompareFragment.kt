package com.example.factory_map_project.ui.compare

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.databinding.FragmentCompareBinding
import com.example.factory_map_project.ui.base.BaseFragment
import com.example.factory_map_project.util.ARG_CONTENT
import com.example.factory_map_project.util.ARG_SECOND_CONTENT
import com.example.factory_map_project.util.CommonUtil.getData
import com.example.factory_map_project.util.adapter.CompareAdapter
import com.example.factory_map_project.util.adapter.IndicatorAdapter
import dagger.hilt.android.AndroidEntryPoint

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
        val remoteList = arguments?.getData<Array<FactoryInfo>>(ARG_CONTENT)?.toList() ?: emptyList()
        val localList = arguments?.getData<Array<FactoryInfo>>(ARG_SECOND_CONTENT)?.toList() ?: emptyList()

        viewModel.compareList.value = remoteList.zip(localList)
    }

    override fun setUI() {
        // RecyclerView 세팅
        setCompareRecyclerView(binding.compareRecyclerview, viewModel.compareList.value, {})
        setIndicatorRecyclerView(binding.checkRecyclerview, viewModel.compareList.value.size)
    }

    override fun setObserver() {

    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun setCompareRecyclerView(recyclerView: RecyclerView, list: List<Pair<FactoryInfo,FactoryInfo>>, onSelect: (Int) -> Unit) {
        PagerSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CompareAdapter(
            list = list,
            onSelect = { postion, data ->

            }
        )
    }

    private fun setIndicatorRecyclerView(recyclerView: RecyclerView, count: Int){
        recyclerView.layoutManager = GridLayoutManager(requireContext(), count, GridLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = IndicatorAdapter(
            array = Array(count){false},
        )
    }
}