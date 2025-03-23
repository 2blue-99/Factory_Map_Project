package com.example.factory_map_project.ui.login

import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.FragmentLoginBinding
import com.example.factory_map_project.ui.base.BaseFragment
import com.example.factory_map_project.util.adapter.IndicatorAdapter
import dagger.hilt.android.AndroidEntryPoint

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
    override fun setData() {

    }

    override fun setUI() {

    }

    override fun setObserver() {



    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************

}