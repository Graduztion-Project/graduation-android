package com.catholic.graduation.presentation.ui.intro.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catholic.graduation.app.DataStoreManager
import com.catholic.graduation.data.model.request.LoginRequest
import com.catholic.graduation.data.repository.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginEvent {
    data object NavigateToBack : LoginEvent()
    data object NavigateToFindPw : LoginEvent()
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

    val idWarningText = MutableStateFlow("")
    val pwWarningText = MutableStateFlow("")
    val loginBtnText = MutableStateFlow("로그인하기")

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")

    fun login() {
        viewModelScope.launch {
            val body = LoginRequest(email = id.value, password = pw.value)
            val result = repository.login(body)
            result.fold(
                onSuccess = {
                    idWarningText.value = ""
                    pwWarningText.value = ""
                    loginSuccess(it.accessToken, it.refreshToken)
                    _event.emit(LoginEvent.GoToMainActivity)
                },
                onFailure = { throwable ->
                    when (throwable) {
                        is LoginError -> {
                            when (throwable.errorCode) {
                                "EmailNotFound" -> {
                                    idWarningText.value = "존재하지 않는 계정입니다"
                                    pwWarningText.value = ""
                                }

                                "IncorrectPassword" -> {
                                    idWarningText.value = ""
                                    pwWarningText.value = "비밀번호가 일치하지 않습니다"
                                }

                                "AccountLocked" -> {
                                    idWarningText.value = "계정이 잠겼습니다"
                                    pwWarningText.value = ""
                                    _event.emit(
                                        LoginEvent.ShowToastMessage(
                                            "남은 시간 -> ${
                                                modifyTime(
                                                    throwable.lockTimeRemainingMillis!!
                                                )
                                            }"
                                        )
                                    )
                                }
                            }
                        }
                    }
                    // todo 아이디, 비밀번호 오류 구분
                    idWarningText.value = "계정 정보를 확인해주세요"
                    pwWarningText.value = "비밀번호를 확인해주세요"

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

    fun navigateToFindPw() {
        viewModelScope.launch {
            _event.emit(LoginEvent.NavigateToFindPw)
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

    private fun modifyTime(time: Long): String {
        val min = (time / 1000) / 60
        val sec = (time / 1000) % 60
        val formattedTime = String.format("%d분 %02d초", min, sec)
        return formattedTime
    }

}

class LoginError(
    val errorCode: String,
    val errorDescription: String,
    val lockTimeRemainingMillis: Long? = null
) : Exception(errorDescription)