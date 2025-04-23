package com.factory.factory_map_project.ui.dialog

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.factory.factory_map_project.databinding.DialogSpinnerBinding
import com.factory.factory_map_project.ui.base.BaseDialog
import com.factory.factory_map_project.util.ARG_CONTENT
import com.factory.factory_map_project.util.adapter.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpinnerDialog: BaseDialog<DialogSpinnerBinding, SpinnerDialogViewModel>(
    DialogSpinnerBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: SpinnerDialogViewModel by viewModels()

    private var list: List<String> = emptyList()
    private var position: Int = 0
    lateinit var onSelect: (Int) -> Unit


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {
        dialog?.setCanceledOnTouchOutside(true)
        // viewModel 데이터 세팅
        list = arguments?.getStringArrayList(ARG_CONTENT) ?: emptyList()
        position = arguments?.getInt(ARG_POSITION) ?: 0
//        viewModel.setSpinnerData(list, position)
    }

    override fun setUI() {
        // 시작 위치 지정
        initRecyclerScrollPosition(position)
        // RecyclerView 세팅
        setRecyclerView(list, position, onSelect)
    }

    override fun setObserver() {}

    override fun onClickNegative() {}

    override fun onClickPositive() {}


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    /**
     * Recyclerview 시작 위치 지정 & 중앙 위치
     *
     * scrollToPositionWithOffset(Int), scrollToPosition(int) 함수는 아이템을 Recyclerview 의 맨 위로 올림
     * 중앙 배치하기 위해 다이알로그의 높이 고정값을 기준으로 위치를 2만큼 뺌
     */
    private fun initRecyclerScrollPosition(position: Int){
        val targetPosition = if(position < 2) 0 else position-2
        val layoutManager = binding.priceSpinner.layoutManager as? LinearLayoutManager ?: return
        layoutManager.scrollToPositionWithOffset(targetPosition, 0)
    }

    private fun setRecyclerView(list: List<String>, position: Int, onSelect: (Int) -> Unit) {
        binding.priceSpinner.adapter = SpinnerAdapter(
            list = list,
            selectedPosition = position,
            onSelect = {
                this.dismiss()
                onSelect(it)
            }
        )
    }


    //**********************************************************************************************
    // Mark: Companion Object
    //**********************************************************************************************
    companion object {

        const val ARG_POSITION = "argument.position"

        fun newInstance(list: List<String>, position: Int, onSelect: (Int) -> Unit): SpinnerDialog {
            return SpinnerDialog().apply {
                this.onSelect = onSelect
                arguments = Bundle().apply {
                    putStringArrayList(ARG_CONTENT, ArrayList(list))
                    putInt(ARG_POSITION, position)
                }
            }
        }
    }
}