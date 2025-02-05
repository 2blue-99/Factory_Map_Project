package com.example.factory_map_project.ui.bottomDialog

import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.BottomDialogDownloadBinding
import com.example.factory_map_project.ui.base.BaseBottomDialog
import com.example.factory_map_project.util.Util.repeatOnFragmentStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DownloadBottomDialog: BaseBottomDialog<BottomDialogDownloadBinding, DownloadViewModel>(
    BottomDialogDownloadBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: DownloadViewModel by viewModels()


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {
        bottomDialog.setCanceledOnTouchOutside(true)
        bottomDialog.behavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        super.onCreateDialog(savedInstanceState) as BottomSheetDialog
//        with(bottomDialog.behavior){
//            peekHeight = 340
//            isHideable = false
//        }
//        bottomDialog.window?.setDimAmount(0f)
//        bottomDialog.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        return bottomDialog
//    }

    override fun setUI() {}

    override fun setObserver() {
        repeatOnFragmentStarted {
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

    companion object {
        fun newInstance(): DownloadBottomDialog {
            return DownloadBottomDialog()
        }
    }
}