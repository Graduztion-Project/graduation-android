package com.catholic.graduation.presentation.ui.intro.password.findpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catholic.graduation.data.model.request.EmailRequest
import com.catholic.graduation.data.repository.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FindPasswordUiState(
    val codeTextVisible: Boolean = false
)

sealed class FindPasswordEvent {
    data class NavigationToChangePw(val token: String) : FindPasswordEvent()
    data object NavigateToBack : FindPasswordEvent()
    data class ShowToastMessage(val msg: String) : FindPasswordEvent()
}

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {
    private val _event = MutableSharedFlow<FindPasswordEvent>()
    val event: SharedFlow<FindPasswordEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(FindPasswordUiState())
    val uiState: StateFlow<FindPasswordUiState> = _uiState.asStateFlow()

    val id = MutableStateFlow("")
    val code = MutableStateFlow("")
    var realCode = "000727"
    var token = ""

    fun sendCode() {
        viewModelScope.launch {
            val result = repository.verificationEmail(EmailRequest(id.value))
            result.fold(
                onSuccess = {
                    realCode = it.verificationCode
                    token = it.verificationToken
                },
                onFailure = {
                    _event.emit(FindPasswordEvent.ShowToastMessage("${it.message}"))
                }
            )
        }
    }

    fun codeConfirm() {
        viewModelScope.launch {
            if (code.value == realCode) {
                _uiState.update { state ->
                    state.copy(
                        codeTextVisible = false
                    )
                }
                _event.emit(FindPasswordEvent.NavigationToChangePw(token))
            } else {
                _uiState.update { state ->
                    state.copy(
                        codeTextVisible = true
                    )
                }
            }
        }
    }


}