package com.catholic.graduation.presentation.ui.intro.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catholic.graduation.R
import com.catholic.graduation.databinding.FragmentLoginBinding
import com.catholic.graduation.databinding.FragmentSignupBinding
import com.catholic.graduation.presentation.base.BaseFragment
import com.catholic.graduation.presentation.ui.intro.login.LoginEvent
import com.catholic.graduation.presentation.ui.intro.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(R.layout.fragment_signup) {

    private val viewModel : SignupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect{

            }
        }
    }

}