package com.example.factory_map_project.ui.dialog

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.DialogInputBinding
import com.example.factory_map_project.ui.base.BaseDialog
import com.example.factory_map_project.util.ARG_CONTENT
import com.example.factory_map_project.util.Util.repeatOnFragmentStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputDialog: BaseDialog<DialogInputBinding, InputDialogViewModel>(
    DialogInputBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: InputDialogViewModel by viewModels()

    lateinit var onSaveData: (String) -> Unit


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {
        dialog?.setCanceledOnTouchOutside(true)
        // viewModel 데이터 세팅
        val text = arguments?.getString(ARG_CONTENT) ?: ""
        viewModel.setData(text)
    }

    override fun setUI() {}

    override fun setObserver() {
        repeatOnFragmentStarted {
            viewModel.eventFlow.collect { event ->
                when(event){
                    is AppEvent.Action<*> -> {
                        when(event.type){
                            ActionType.NEGATIVE -> {
                                dismiss()
                            }
                            ActionType.CONFIRM -> {
                                onSaveData(binding.editTxt.text.toString())
                                dismiss()
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    override fun onClickNegative() {}

    override fun onClickPositive() {}

    //**********************************************************************************************
    // Mark: Companion Object
    //**********************************************************************************************
    companion object {
        fun newInstance(text: String, onSave: (String) -> Unit): InputDialog {
            return InputDialog().apply {
                this.onSaveData = onSave
                arguments = Bundle().apply {
                    putString(ARG_CONTENT, text)
                }
            }
        }
    }
}