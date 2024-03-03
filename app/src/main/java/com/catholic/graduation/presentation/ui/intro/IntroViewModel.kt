package com.catholic.graduation.presentation.ui.intro

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed class IntroEvent {
}

@HiltViewModel
class IntroViewModel @Inject constructor() : ViewModel(){
    private val _event = MutableSharedFlow<IntroEvent>()
    val event: SharedFlow<IntroEvent> = _event.asSharedFlow()

}