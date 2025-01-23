package com.example.factory_map_project.ui.setting

import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.FragmentSettingBinding
import com.example.factory_map_project.ui.base.BaseFragment
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when(event){
                    is AppEvent.GoBack -> { mainActivity().onBackPressed() }
                    is AppEvent.Action<*> -> {
                        when(event.type){
                            ActionType.EXCLUSION -> {  }
                            ActionType.SENSITIVITY -> {  }
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