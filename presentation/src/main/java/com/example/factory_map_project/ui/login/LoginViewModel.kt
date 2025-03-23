package com.example.factory_map_project.ui.login

import com.example.domain.repo.FactoryRepository
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.InitialMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val factoryRepository: FactoryRepository
) : BaseViewModel() {
    var isLoginButton = InitialMutableLiveData<Boolean>(false)
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************



    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
    fun onClickLoginButton(){

    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************

}