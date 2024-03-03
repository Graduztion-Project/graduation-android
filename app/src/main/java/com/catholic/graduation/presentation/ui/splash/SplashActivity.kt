package com.catholic.graduation.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.catholic.graduation.R
import com.catholic.graduation.databinding.ActivitySplashBinding
import com.catholic.graduation.presentation.base.BaseActivity
import com.catholic.graduation.presentation.ui.intro.IntroActivity
import com.catholic.graduation.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            delay(2000)
            viewModel.getAutoLogin()
        }
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SplashUiEvent.NavigateToIntro -> toIntroActivity()
                    is SplashUiEvent.NavigateToMain -> toMainActivity()
                }
            }
        }
    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }
}