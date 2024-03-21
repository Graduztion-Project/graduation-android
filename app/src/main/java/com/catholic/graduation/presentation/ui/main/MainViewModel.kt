package com.catholic.graduation.presentation.ui.main

import androidx.lifecycle.ViewModel
import com.catholic.graduation.data.repository.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val introRepository: IntroRepository
): ViewModel() {


}