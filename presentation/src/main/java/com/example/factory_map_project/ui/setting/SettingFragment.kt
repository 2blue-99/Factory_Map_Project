package com.example.factory_map_project.ui.setting

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.factory_map_project.databinding.FragmentSettingBinding
import com.example.factory_map_project.ui.base.BaseFragment
import com.example.factory_map_project.util.Util.repeatOnFragmentStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(
    FragmentSettingBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: SettingViewModel by viewModels()


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {

    }

    override fun setUI() {

    }

    override fun setObserver() {
        repeatOnFragmentStarted {
            viewModel.eventFlow.collect { event ->
                when(event){
                    is AppEvent.GoBack -> { findNavController().popBackStack() }
                    is AppEvent.ShowSpinnerDialog -> {
                        mainActivity().showSpinnerDialog(event.content, event.position, event.onSelect)
                    }
                    is AppEvent.Action<*> -> {
                        when(event.type){
                            ActionType.EXCLUSION -> {

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
}