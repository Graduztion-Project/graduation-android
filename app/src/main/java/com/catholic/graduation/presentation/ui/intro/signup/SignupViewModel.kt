package com.catholic.graduation.presentation.ui.intro.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catholic.graduation.data.model.request.EmailRequest
import com.catholic.graduation.data.model.request.SignUpRequest
import com.catholic.graduation.data.repository.IntroRepository
import com.catholic.graduation.presentation.ui.InputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignupUiState(
    val idState: InputState = InputState.Empty,
    val pwState: InputState = InputState.Empty,
    val pwChkState: InputState = InputState.Empty,
    val codeState: InputState = InputState.Empty,
    val codeVisible: Boolean = false
)

sealed class SignupEvent {
    data object NavigateToBack : SignupEvent()
    data class ShowToastMessage(val msg: String) : SignupEvent()
}

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {
    private val _event = MutableSharedFlow<SignupEvent>()
    val event: SharedFlow<SignupEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")
    val pwChk = MutableStateFlow("")
    private var checkedEmail = ""
    val idAvailable = MutableStateFlow(false)
    val duplicateAvailable = MutableStateFlow(false)
    private val pwAvailable = MutableStateFlow(false)
    private val pwChkAvailable = MutableStateFlow(false)

    val code = MutableStateFlow("")
    var realCode = -1


    val isDataReady = combine(idAvailable, pwAvailable, pwChkAvailable) { id, pw, pwChk ->
        id && pw && pwChk
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    init {
        idObserve()
        pwObserve()
        pwChkObserve()
    }

    private fun idObserve() {
        id.onEach {
            if (it.isNotBlank()) {
                if (isIdValid(id.value)) {
                    _uiState.update { state ->
                        state.copy(
                            idState = InputState.Success("이메일 중복을 확인해주세요")
                        )
                    }
                    idAvailable.value = true
                    duplicateAvailable.value = false
                } else {
                    _uiState.update { state ->
                        state.copy(
                            idState = InputState.Error("이메일 형식을 지켜주세요")
                        )
                    }
                    idAvailable.value = false
                    duplicateAvailable.value = false
                }

            } else {
                _uiState.update { state ->
                    state.copy(
                        idState = InputState.Empty
                    )
                }
                idAvailable.value = false
                duplicateAvailable.value = false
            }
        }.launchIn(viewModelScope)
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

    fun emailCheck() {
        viewModelScope.launch {
            val result = repository.duplicate(EmailRequest(id.value))
            result.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            idState = InputState.Success("사용가능한 이메일입니다.")
                        )
                    }
                    duplicateAvailable.value = true
                    checkedEmail = id.value
                },
                onFailure = {
                    _uiState.update { state ->
                        state.copy(
                            idState = InputState.Error("중복된 이메일입니다.")
                        )
                    }
                }
            )
        }
    }

    fun emailConfirm() {
        viewModelScope.launch {
            val result = repository.verification(EmailRequest(id.value))
            result.fold(
                onSuccess = {
                    realCode = it.toInt()
                    _uiState.update { state ->
                        state.copy(
                            codeVisible = true
                        )
                    }
                },
                onFailure = {
                    _event.emit(SignupEvent.ShowToastMessage("${it.message}"))
                }
            )
        }
    }

    fun codeConfirm(){
        viewModelScope.launch {
            if(code.value.toInt() == realCode){
                _uiState.update { state ->
                    state.copy(
                        codeState = InputState.Success("인증 완료")
                    )
                }
            }else{
                _uiState.update { state ->
                    state.copy(
                        codeState = InputState.Error("인증 코드를 다시 확인해주세요")
                    )
                }
            }
        }
    }

    fun completeSignup() {
        viewModelScope.launch {
            if (isDataReady.value) {
                val body = SignUpRequest(name = "test", email = id.value, password = pw.value)
                val result = repository.signUp(body)
                result.fold(
                    onSuccess = { body ->
                        navigateToBack()
                    },
                    onFailure = {
                        _event.emit(SignupEvent.ShowToastMessage(it.message ?: "Unknown error"))
                    }
                )
            } else {
                _event.emit(SignupEvent.ShowToastMessage("아이디, 비밀번호를 확인해주세요"))
            }
        }
        navigateToBack()
    }

    private fun isIdValid(id: String): Boolean {
        val idPattern = Regex(REGEX_EMAIL)
        return idPattern.matches(id)
    }

    private fun isPasswordValid(pw: String): Boolean {
        val passwordPattern = Regex(REGEX_PWD)
        return passwordPattern.matches(pw)
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SignupEvent.NavigateToBack)
        }
    }

    companion object {
        private const val REGEX_EMAIL = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"
        private const val REGEX_PWD = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()\\-+]).{9,22}$"
    }

}