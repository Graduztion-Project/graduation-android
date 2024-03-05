package com.catholic.graduation.presentation.ui.intro.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.catholic.graduation.R
import com.catholic.graduation.databinding.FragmentLoginBinding
import com.catholic.graduation.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel : LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect{
                when(it) {
                    LoginEvent.GoToMainActivity -> TODO()
                    LoginEvent.NavigateToBack -> findNavController().navigateUp()
                    LoginEvent.NavigateToFindAccount -> TODO()
                    LoginEvent.NavigateToSignUp -> findNavController().toSignup()
                    is LoginEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun NavController.toSignup(){
        val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        navigate(action)
    }

}