package com.catholic.graduation.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catholic.graduation.app.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUiEvent {
    data object NavigateToMain : SplashUiEvent()
    data object NavigateToIntro : SplashUiEvent()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    private val _event = MutableSharedFlow<SplashUiEvent>()
    val event: SharedFlow<SplashUiEvent> = _event.asSharedFlow()

    fun getAutoLogin() {
        viewModelScope.launch {
            dataStoreManager.getAutoLogin().collect { autoLogin ->
                dataStoreManager.getAccessToken().collect { accessToken ->
                    val value = if (autoLogin == true && accessToken != "") {
                        _event.emit(SplashUiEvent.NavigateToMain)
                    } else {
                        _event.emit(SplashUiEvent.NavigateToIntro)
                    }
                }
            }
        }
    }
}