package com.factory.factory_map_project.ui.bottomDialog

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.factory.factory_map_project.databinding.BottomDialogMarkerBinding
import com.factory.factory_map_project.ui.base.BaseBottomDialog
import com.factory.factory_map_project.util.ARG_CONTENT
import com.factory.factory_map_project.util.CommonUtil.getData
import com.factory.factory_map_project.util.CommonUtil.moveCall
import com.factory.factory_map_project.util.CommonUtil.moveTMap
import com.factory.factory_map_project.util.CommonUtil.repeatOnFragmentStarted
import com.factory.factory_map_project.util.event.ActionType
import com.factory.factory_map_project.util.event.AppEvent
import com.factory.factory_map_project.util.map.FactoryCluster
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MarkerBottomDialog: BaseBottomDialog<BottomDialogMarkerBinding, MarkerViewModel>(
    BottomDialogMarkerBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: MarkerViewModel by viewModels()

    private lateinit var updateCluster: (FactoryCluster) -> Unit
    private lateinit var deleteCluster: () -> Unit

    private lateinit var cluster: FactoryCluster


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {
        arguments?.getData<FactoryCluster>(ARG_CONTENT)?.let { data ->
            cluster = data
            viewModel.loadData(data)
            Timber.d("data : $data")
        }
    }

    override fun setUI() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun setObserver() {
        repeatOnFragmentStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.Action<*> -> {
                        when(event.type){
                            ActionType.CALL -> {
                                moveCall(requireContext(),event.input.toString())
                            }
                            ActionType.MAP -> {
                                if(event.input is FactoryCluster){
                                    moveTMap(requireContext(), event.input, { mainActivity().showToast("앱을 실행할 수 없습니다.") })
                                }
                            }
                            ActionType.NEGATIVE -> onClickNegative()
                            ActionType.CONFIRM -> onClickPositive()
                            ActionType.DELETE -> {
                                deleteCluster()
                                dismiss()
                            }
                            else -> {}
                        }
                    }
                    is AppEvent.ShowPopup -> mainActivity().dialogManager.showMessageDialog(event)
                    is AppEvent.ShowInputDialog -> mainActivity().showInputDialog(event.text, event.onSaveData)
                    else -> {}
                }
            }
        }
    }

    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    override fun onClickNegative() {
        dismiss()
    }

    override fun onClickPositive() {
        viewModel.uiData.value?.let { data ->
            val origin = viewModel.originCluster
            val change = data.copy(
                isCheck = binding.checkBox.isChecked,
                memo = binding.memo.text.toString(),
            )
            if (origin?.hashCode() != change.hashCode()) {
                updateCluster(change)
                Toast.makeText(requireContext(), "저장되었어요.", Toast.LENGTH_SHORT).show()
            } else {
                Timber.d("변경안됨")
            }
        }
        dismiss()
    }


    //**********************************************************************************************
    // Mark: Companion Object
    //**********************************************************************************************
    companion object {
        fun newInstance(
            content: FactoryCluster,
            updateCluster: (FactoryCluster) -> Unit,
            deleteCluster: () -> Unit
        ): MarkerBottomDialog {
            return MarkerBottomDialog().apply {
                this.updateCluster = updateCluster
                this.deleteCluster = deleteCluster
                arguments = Bundle().apply {
                    putSerializable(ARG_CONTENT, content)
                }
            }
        }
    }
}

