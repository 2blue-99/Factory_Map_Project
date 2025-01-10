package com.example.factory_map_project.ui.dialog

import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.DownloadBottomDialogBinding
import com.example.factory_map_project.ui.base.BaseBottomDialog
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DownloadBottomDialog: BaseBottomDialog<DownloadBottomDialogBinding, DownloadViewModel>(
    DownloadBottomDialogBinding::inflate
) {

    override val viewModel: DownloadViewModel by viewModels()

    override fun setData() {
        dialog?.setCanceledOnTouchOutside(false)
        (dialog as? BottomSheetDialog)?.behavior?.isHideable = false
    }

    override fun setUI() {}

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun setObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.ShowLoading -> mainActivity().setLoading(event.state)
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

//        viewModel.uiData.observe(viewLifecycleOwner) { list ->
//            val geocoder = Geocoder(requireContext())
//            repeat(list.size){ position ->
//                geocoder.getFromLocationName(list[position].roadAddress, 1){ result ->
//                    Timber.d("${result[0]}")
//                    binding.successCount.text = (++count).toString()
//                }
//            }
//            Timber.d("")
//        }
    }

    override fun onClickNegative() {
        dismiss()
    }

    override fun onClickPositive() {
        dismiss()
    }
}