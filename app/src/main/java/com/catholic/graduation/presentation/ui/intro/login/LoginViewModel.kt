package com.catholic.graduation.presentation.ui.intro.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catholic.graduation.app.DataStoreManager
import com.catholic.graduation.data.model.request.LoginRequest
import com.catholic.graduation.data.repository.IntroRepository
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
class LoginViewModel @Inject constructor(
    private val repository: IntroRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _event = MutableSharedFlow<LoginEvent>()
    val event: SharedFlow<LoginEvent> = _event.asSharedFlow()

    val warningText = MutableStateFlow("")

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")

    fun login() {
        viewModelScope.launch {
            val body = LoginRequest(email = id.value,password = pw.value)
            val result = repository.login(body)
            result.fold(
                onSuccess = {
                    warningText.value = ""
                    loginSuccess(it.accessToken, it.refreshToken)
                    _event.emit(LoginEvent.GoToMainActivity)
                },
                onFailure = {
                    warningText.value = "계정 정보를 확인해주세요"
                }
            )
        }
    }

    private fun loginSuccess(access: String, refresh: String) {
        viewModelScope.launch {
            dataStoreManager.putAccessToken(access)
            dataStoreManager.putRefreshToken(refresh)
            _event.emit(LoginEvent.GoToMainActivity)
        }
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