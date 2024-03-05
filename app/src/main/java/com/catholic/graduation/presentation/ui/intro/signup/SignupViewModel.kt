package com.catholic.graduation.presentation.ui.intro.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catholic.graduation.presentation.ui.InputState
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

data class SignupUiState(
    val idState: InputState = InputState.Empty,
    val pwState: InputState = InputState.Empty,
    val chkpwState: InputState = InputState.Empty
)

sealed class SignupEvent{

}

@HiltViewModel
class SignupViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<SignupEvent>()
    val event: SharedFlow<SignupEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    val pw_chk = MutableStateFlow("")

    init {
        idObserve()
        pwObserve()
        pwChkObserve()
    }

    private fun idObserve() {
        id.onEach {
            if(it.isNotBlank()){
                if(isIdValid(id.value)){
                    _uiState.update { state ->
                        state.copy(
                            idState = InputState.Success("적합한 아이디 입니다")
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            idState = InputState.Error("이메일 형식을 지켜주세요")
                        )
                    }
                }

            }
        }.launchIn(viewModelScope)
    }

    private fun pwObserve() {
        pw.onEach {
            if(it.isNotBlank()) {
                if (isPasswordValid(pw.value)) {
                    _uiState.update { state ->
                        state.copy(
                            pwState = InputState.Success("적합한 비밀번호 입니다")
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            pwState = InputState.Error("영문과 숫자 조합 8자리 이상 입력하세요.")
                        )
                    }
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        pwState = InputState.Empty
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun pwChkObserve() {
        pw_chk.onEach {
            if(it.isNotBlank()) {
                if(pw.value == pw_chk.value){
                    _uiState.update{state ->
                        state.copy(
                            chkpwState = InputState.Success("")
                        )
                    }
                } else {
                    _uiState.update{state ->
                        state.copy(
                            chkpwState = InputState.Error("비밀번호를 확인해주세요")
                        )
                    }
                }

            } else {
                _uiState.update { state ->
                    state.copy(
                        chkpwState = InputState.Empty
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun isIdValid(id: String): Boolean {
        val idPattern = Regex(REGEX_EMAIL)
        return idPattern.matches(id)
    }

    private fun isPasswordValid(pw: String): Boolean {
        val passwordPattern = Regex(REGEX_PWD)
        return passwordPattern.matches(pw)
    }

    companion object{
        private const val REGEX_EMAIL = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"

        private const val REGEX_PWD = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()\\-+]).{8,15}$"
    }

}