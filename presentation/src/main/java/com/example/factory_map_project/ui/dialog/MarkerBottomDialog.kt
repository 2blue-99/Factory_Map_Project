package com.example.factory_map_project.ui.dialog

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.MarkerBottomDialogBinding
import com.example.factory_map_project.ui.base.BaseBottomDialog
import com.example.factory_map_project.util.Util.moveCall
import com.example.factory_map_project.util.Util.moveTMap
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MarkerBottomDialog(
    private val onChangeVisit: () -> Unit,
    private val onChangeNotVisit: () -> Unit
): BaseBottomDialog<MarkerBottomDialogBinding, MarkerViewModel>(
    MarkerBottomDialogBinding::inflate
) {

    override val viewModel: MarkerViewModel by viewModels()

    override fun setData() {
        val data = this.arguments?.getSerializable(ARG_CONTENT)
        if(data is FactoryCluster){
            viewModel.loadData(data)
        }
    }

    override fun setUI() {
        binding.test.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                onChangeVisit()
            }else{
                onChangeNotVisit()
            }
        }
    }

    override fun setObserver() {
        repeatOnStarted {
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
                                    moveTMap(requireContext(), event.input)
                                }
                            }
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

        fun newInstance(
            content: FactoryCluster,
            onChangeVisit: () -> Unit,
            onChangeNotVisit: () -> Unit
        ): MarkerBottomDialog {
            val dialog = MarkerBottomDialog(onChangeVisit, onChangeNotVisit)
            val args = Bundle().apply {
                putSerializable(ARG_CONTENT, content)
            }
            dialog.arguments = args
            return dialog
        }
    }
}