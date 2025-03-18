package com.example.factory_map_project.ui.compare

import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.example.factory_map_project.util.CommonUtil.repeatOnFragmentStarted
import com.example.factory_map_project.util.SELECT_NONE
import com.example.factory_map_project.util.adapter.CompareAdapter
import com.example.factory_map_project.util.adapter.IndicatorAdapter
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
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

    private val indicatorAdapter = IndicatorAdapter()


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {
        val remoteList = arguments?.getData<Array<FactoryInfo>>(ARG_CONTENT)?.toList() ?: emptyList()
        val localList = arguments?.getData<Array<FactoryInfo>>(ARG_SECOND_CONTENT)?.toList() ?: emptyList()

        viewModel.setData(remoteList.zip(localList))
    }

    override fun setUI() {
        // RecyclerView 세팅
        setCompareRecyclerView(binding.compareRecyclerview, viewModel.compareList.value.toList())
        setIndicatorRecyclerView(binding.checkRecyclerview, viewModel.compareList.value.size)
        setScrollLayout(binding.scrollLayout)
    }

    override fun setObserver() {
        repeatOnFragmentStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.MovePage -> {
                        findNavController().navigate(event.id, event.data)
                    }
                    is AppEvent.ShowToast -> {
                        mainActivity().showToast(event.message)
                    }
                    is AppEvent.Action<*> -> {
                        when(event.type){
                            ActionType.CONFIRM -> onClickPositive()
                            else -> {}
                        }
                    }
                    else -> {}
                }
            }
        }

        viewModel.selectList.observe(viewLifecycleOwner){ list ->
            // 하단 인디케이터 데이터 업데이트
            indicatorAdapter.list = list.map { it.first }

            // 모두 선택했을 경우, 확인 버튼 활성화
            if(!list.map { it.first }.contains(SELECT_NONE)){
                viewModel.isSelectAll.value = true
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun setCompareRecyclerView(recyclerView: RecyclerView, list: List<Pair<FactoryInfo,FactoryInfo>>) {
        PagerSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CompareAdapter(
            list = list,
            onSelect = { postion, data ->
                viewModel.selectList.value = viewModel.selectList.value.apply {
                    viewModel.selectList.value[postion] = data
                }
            }
        )
    }

    private fun setIndicatorRecyclerView(recyclerView: RecyclerView, count: Int){
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = indicatorAdapter
    }

    /**
     * 비교 Recyclerview 높이 = 전체 높이의 70%
     */
    private fun setScrollLayout(scrollView: NestedScrollView){
        val height = resources.displayMetrics.heightPixels
        scrollView.layoutParams.height = (height * 0.7).toInt()
    }

    private fun onClickPositive(){
        findNavController().popBackStack()
    }
}