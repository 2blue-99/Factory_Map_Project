package com.example.factory_map_project.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.DownLoadBottomSheetBinding
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.BaseEvent
import com.example.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DownloadDialog: BaseBottomDialog<DownLoadBottomSheetBinding, DownLoadViewModel>(
    DownLoadBottomSheetBinding::inflate
) {

    override val viewModel: DownLoadViewModel by viewModels()

    override fun setData() {
        val data = this.arguments
//        if(data is FactoryCluster){
//            viewModel.loadData(data)
//        }
    }

    override fun setUI() {}

    override fun setObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is BaseEvent.MovePage -> {}
                    else -> {}
                }
            }
        }
    }

    override fun setListener() {
        binding.bottomBtn.negativeClick = onClickNegative()
        binding.bottomBtn.positiveClick = onClickPositive()
    }

    override fun onClickNegative() = View.OnClickListener {
        dismiss()
    }

    override fun onClickPositive() =  View.OnClickListener {
        dismiss()
    }


    companion object {

        private const val ARG_CONTENT = "content"

        fun newInstance(content: String): DownloadDialog {
            val fragment = DownloadDialog()
            val args = Bundle().apply {
                putString(ARG_CONTENT, content)
            }
            fragment.arguments = args
            return fragment
        }
    }
}