package com.example.factory_map_project.ui.dialog

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.MarkerBottomSheetBinding
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MarkerDialog: BaseBottomDialog<MarkerBottomSheetBinding, MarkerViewModel>(
    MarkerBottomSheetBinding::inflate
) {

    override val viewModel: MarkerViewModel by viewModels()

    override fun setData() {
        val data = this.arguments?.getSerializable(ARG_CONTENT)
        if(data is FactoryCluster){
            viewModel.loadData(data)
        }
    }

    override fun setUI() {}

    override fun setObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.Action -> {
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
    }

    override fun onClickNegative() {
        dismiss()
    }

    override fun onClickPositive() {
        dismiss()
    }


    companion object {

        private const val ARG_CONTENT = "content"

        fun newInstance(content: FactoryCluster): MarkerDialog {
            val dialog = MarkerDialog()
            val args = Bundle().apply {
                putSerializable(ARG_CONTENT, content)
            }
            dialog.arguments = args
            return dialog
        }
    }
}