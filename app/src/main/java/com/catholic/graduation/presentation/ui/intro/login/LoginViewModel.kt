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

    val failureCount = MutableStateFlow(0)

    private fun startLoginTimer() {
        viewModelScope.launch {
            for(time in 300 downTo 0) {
                val minutes = time / 60
                val seconds = time % 60
                loginBtnText.value = String.format("%02d:%02d", minutes, seconds)
                delay(1000) // 1초 대기
            }
            // 타이머가 끝나면 로그인 버튼의 텍스트와 실패 횟수를 초기화
            loginBtnText.value = "로그인하기"
            failureCount.value = 0
        }
    }

    fun login() {
        viewModelScope.launch {
            if (failureCount.value >= 5) {
                _event.emit(LoginEvent.ShowToastMessage("로그인 시도가 일시적으로 제한되었습니다. 잠시 후 다시 시도해 주세요"))
            } else {
                val body = LoginRequest(email = id.value, password = pw.value)
                val result = repository.login(body)
                result.fold(
                    onSuccess = {
                        idWarningText.value = ""
                        pwWarningText.value = ""
                        failureCount.value = 0
                        loginSuccess(it.accessToken, it.refreshToken)
                        _event.emit(LoginEvent.GoToMainActivity)
                    },
                    onFailure = {
                        // todo 아이디, 비밀번호 오류 구분
                        failureCount.value += 1
                        idWarningText.value = "계정 정보를 확인해주세요"
                        pwWarningText.value = "비밀번호를 확인해주세요"

                        if (failureCount.value == 5) {
                            startLoginTimer()
                        }
                    }
                )
            }
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

}