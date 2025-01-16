package com.example.factory_map_project.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.MarkerBottomDialogBinding
import com.example.factory_map_project.ui.base.BaseBottomDialog
import com.example.factory_map_project.util.ARG_CONTENT
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.Util.moveCall
import com.example.factory_map_project.util.Util.moveTMap
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MarkerBottomDialog: BaseBottomDialog<MarkerBottomDialogBinding, MarkerViewModel>(
    MarkerBottomDialogBinding::inflate
) {
    override val viewModel: MarkerViewModel by viewModels()

    private lateinit var updateCluster: (FactoryCluster) -> Unit
    private lateinit var deleteCluster: () -> Unit

    private lateinit var cluster: FactoryCluster

    override fun setData() {
        val data = this.arguments?.getSerializable(ARG_CONTENT)
        if(data is FactoryCluster){
            cluster = data
            viewModel.loadData(data)
        }
    }

    override fun setUI() {
//        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
//            Timber.d("isCheck : $isChecked")
//            cluster = cluster.copy(isClick = isChecked)
//        }
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
                            ActionType.NEGATIVE -> onClickNegative()
                            ActionType.CONFIRM -> onClickPositive()
                            ActionType.DELETE -> {
                                deleteCluster()
                                dismiss()
                            }
                            else -> {}
                        }
                    }
                    is AppEvent.ShowPopup -> mainActivity().showDialog(event)
                    else -> {}
                }
            }
        }
    }

    override fun onClickNegative() {
        dismiss()
    }

    override fun onClickPositive() {
        updateCluster(
            cluster.copy(
                isClick = binding.checkBox.isChecked,
                memo = binding.memo.text.toString()
            )
        )
        Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
        dismiss()
    }

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