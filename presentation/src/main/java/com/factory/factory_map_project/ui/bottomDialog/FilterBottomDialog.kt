package com.factory.factory_map_project.ui.bottomDialog

import androidx.fragment.app.viewModels
import com.factory.domain.type.SelectType
import com.factory.factory_map_project.databinding.BottomDialogFilterBinding
import com.factory.factory_map_project.ui.base.BaseBottomDialog
import com.factory.factory_map_project.util.CommonUtil.repeatOnFragmentStarted
import com.factory.factory_map_project.util.adapter.PickerAdapter
import com.factory.factory_map_project.util.event.ActionType
import com.factory.factory_map_project.util.event.AppEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FilterBottomDialog: BaseBottomDialog<BottomDialogFilterBinding, FilterViewModel>(
    BottomDialogFilterBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: FilterViewModel by viewModels()

    private lateinit var pickerAdapter: PickerAdapter

    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {
        bottomDialog.setCanceledOnTouchOutside(true)
        bottomDialog.behavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun setUI() {
        setRecyclerview()
    }

    override fun setObserver() {
        repeatOnFragmentStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.ShowLoading -> {
                        Timber.d("ㅇㅇ")
                        mainActivity().setLoading(event.state)
                    }
                    is AppEvent.ShowToast -> {
                        mainActivity().showToast(event.message)
                    }
                    is AppEvent.Action<*> -> {
                        when(event.type){
                            ActionType.NEGATIVE -> onClickPositive()
                            ActionType.CONFIRM -> onClickNegative()
                            else -> {}
                        }
                    }
                    else -> {}
                }
            }
        }

        viewModel.filterList.observe(viewLifecycleOwner){
            pickerAdapter.inputList = it.reversed()
        }

        binding.targetPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            viewModel.targetWord.value = SelectType.toType(newVal).title
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    override fun onClickNegative() {
        dismiss()
    }

    override fun onClickPositive() {
        dismiss()
    }

    private fun setRecyclerview(){
        pickerAdapter = PickerAdapter {
            viewModel.deleteTarget(it)
            mainActivity().showToast("삭제 되었어요.")
        }
        binding.priceSpinner.adapter = pickerAdapter
    }

    companion object {
        fun newInstance(): FilterBottomDialog {
            return FilterBottomDialog()
        }
    }
}