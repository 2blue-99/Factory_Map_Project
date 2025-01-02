package com.example.factory_map_project.ui.dialog

import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.DownloadBottomDialogBinding
import com.example.factory_map_project.ui.base.BaseBottomDialog
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DownloadBottomDialog: BaseBottomDialog<DownloadBottomDialogBinding, DownloadViewModel>(
    DownloadBottomDialogBinding::inflate
) {

    override val viewModel: DownloadViewModel by viewModels()

    override fun setData() {}

    override fun setUI() {}

    override fun setObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.ShowLoading -> mainActivity.setLoading(event.state)
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

    override fun onClickNegative() {
        dismiss()
    }

    override fun onClickPositive() {
        dismiss()
    }
}