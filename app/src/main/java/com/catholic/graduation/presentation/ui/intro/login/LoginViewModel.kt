package com.catholic.graduation.presentation.ui.intro.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginEvent {
    data object NavigateToBack : LoginEvent()
    data object NavigateToFindAccount : LoginEvent()
    data object NavigateToSignUp : LoginEvent()
    data object GoToMainActivity : LoginEvent()
    data class ShowToastMessage(val msg: String) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<LoginEvent>()
    val event: SharedFlow<LoginEvent> = _event.asSharedFlow()

    val warningText = MutableStateFlow("")

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")

    fun login(){
        // TODO 로그인 api 연결
    }

    fun navigateToFindAccount() {
        viewModelScope.launch {
            _event.emit(LoginEvent.NavigateToFindAccount)
        }
    }

    fun navigateToSignUp() {
        viewModelScope.launch {
            _event.emit(LoginEvent.NavigateToSignUp)
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(LoginEvent.NavigateToBack)
        }
    }

}