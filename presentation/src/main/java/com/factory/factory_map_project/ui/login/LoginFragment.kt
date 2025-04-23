package com.factory.factory_map_project.ui.login

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.factory.factory_map_project.databinding.FragmentLoginBinding
import com.factory.factory_map_project.ui.base.BaseFragment
import com.factory.factory_map_project.util.CommonUtil.repeatOnFragmentStarted
import com.factory.factory_map_project.util.event.ActionType
import com.factory.factory_map_project.util.event.AppEvent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
    FragmentLoginBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: LoginViewModel by viewModels()


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {}

    override fun setUI() {}

    override fun setObserver() {
        repeatOnFragmentStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.ShowToast -> {
                        mainActivity().showToast(event.message)
                    }

                    is AppEvent.ShowLoading -> {
                        mainActivity().setLoading(event.state)
                    }

                    is AppEvent.Action<*> -> {
                        when(event.type){
                            ActionType.CONFIRM -> onClickPositive()
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
    private fun onClickPositive(){
        findNavController().popBackStack()
    }
}