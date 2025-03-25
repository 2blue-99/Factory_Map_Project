package com.example.factory_map_project.ui.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.datastore.UserDataStore
import com.example.domain.repo.FireStoreRepository
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val fireStoreRepository: FireStoreRepository
) : BaseViewModel() {
    var autoLogin = MutableLiveData<Boolean>().apply {
        modelScope.launch {
            value = userDataStore.autoLoginFlow.first()
        }
    }

    var userId = MutableLiveData<String>("")

    var userPassword = MutableLiveData<String>("")

    var isLoginButton = MediatorLiveData<Boolean>().apply {
        addSource(userId) { setButton() }
        addSource(userPassword) { setButton() }
    }


    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************



    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
    fun onClickLoginButton(){
        modelScope.launch {
            if(isLoginButton.value == false){
                emitEvent(AppEvent.ShowToast("아이디 또는 비밀번호를 입력해주세요."))
            }else{
                emitEvent(AppEvent.ShowLoading(true))
                val isSuccess = fireStoreRepository.login(userId.value!!, userPassword.value!!)
                if(isSuccess){
                    emitEvent(AppEvent.ShowToast("로그인 성공"))
                    emitEvent(AppEvent.Action(ActionType.CONFIRM, null))
                }else{
                    emitEvent(AppEvent.ShowToast("존재하지 않는 계정이에요."))
                }
                emitEvent(AppEvent.ShowLoading(false))
            }
        }

    }

    fun onClickAutoLogin(state: Boolean){
        modelScope.launch {
            userDataStore.setAutoLogin(state)
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun setButton(){
        isLoginButton.value = userId.value!!.isNotEmpty() && userPassword.value!!.isNotEmpty()
    }
}