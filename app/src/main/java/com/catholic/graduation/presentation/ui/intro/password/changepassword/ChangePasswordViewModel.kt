package com.catholic.graduation.presentation.ui.intro.password.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catholic.graduation.data.repository.IntroRepository
import com.catholic.graduation.presentation.ui.InputState
import com.catholic.graduation.presentation.ui.intro.password.findpassword.FindPasswordEvent
import com.catholic.graduation.presentation.ui.intro.password.findpassword.FindPasswordUiState
import com.catholic.graduation.presentation.ui.intro.signup.SignupViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ChangePasswordUiState(
    val pwState: InputState = InputState.Empty,
    val pwChkState: InputState = InputState.Empty
)

sealed class ChangePasswordEvent {
    data class NavigationToChangePw(val token: String) : ChangePasswordEvent()
    data object NavigateToBack : ChangePasswordEvent()
    data class ShowToastMessage(val msg: String) : ChangePasswordEvent()
}

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {
    private val _event = MutableSharedFlow<ChangePasswordEvent>()
    val event: SharedFlow<ChangePasswordEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    val pw = MutableStateFlow("")
    val pwChk = MutableStateFlow("")
    private val pwAvailable = MutableStateFlow(false)
    private val pwChkAvailable = MutableStateFlow(false)

    init {
        pwObserve()
        pwChkObserve()
    }

    private fun pwObserve() {
        pw.onEach {
            if (it.isNotBlank()) {
                if (isPasswordValid(pw.value)) {
                    _uiState.update { state ->
                        state.copy(
                            pwState = InputState.Success("적합한 비밀번호 입니다")
                        )
                    }
                    pwAvailable.value = true
                } else {
                    _uiState.update { state ->
                        state.copy(
                            pwState = InputState.Error("영문과 숫자 조합 8자리 이상 입력하세요.")
                        )
                    }
                    pwAvailable.value = false
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        pwState = InputState.Empty
                    )
                }
                pwAvailable.value = false
            }

        }.launchIn(viewModelScope)
    }

    private fun pwChkObserve() {
        pwChk.onEach {
            if (it.isNotBlank()) {
                if (pw.value == pwChk.value) {
                    _uiState.update { state ->
                        state.copy(
                            pwChkState = InputState.Success("")
                        )
                    }
                    pwChkAvailable.value = true
                } else {
                    _uiState.update { state ->
                        state.copy(
                            pwChkState = InputState.Error("비밀번호를 확인해주세요")
                        )
                    }
                    pwChkAvailable.value = false
                }

            } else {
                _uiState.update { state ->
                    state.copy(
                        pwChkState = InputState.Empty
                    )
                }
                pwChkAvailable.value = false
            }
        }.launchIn(viewModelScope)
    }

    private fun isPasswordValid(pw: String): Boolean {
        val passwordPattern = Regex(REGEX_PWD)
        return passwordPattern.matches(pw)
    }

    companion object {
        private const val REGEX_EMAIL = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"
        private const val REGEX_PWD = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()\\-+]).{9,22}$"
    }

}