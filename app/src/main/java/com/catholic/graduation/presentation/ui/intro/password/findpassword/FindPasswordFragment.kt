package com.catholic.graduation.presentation.ui.intro.password.findpassword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.catholic.graduation.R
import com.catholic.graduation.databinding.FragmentFindPasswordBinding
import com.catholic.graduation.databinding.FragmentSignupBinding
import com.catholic.graduation.presentation.base.BaseFragment
import com.catholic.graduation.presentation.ui.intro.signup.SignupEvent
import com.catholic.graduation.presentation.ui.intro.signup.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPasswordFragment : BaseFragment<FragmentFindPasswordBinding>(R.layout.fragment_find_password) {

    private val viewModel : FindPasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    FindPasswordEvent.NavigateToBack -> findNavController().navigateUp()
                    is FindPasswordEvent.ShowToastMessage -> showToastMessage(it.msg)
                    is FindPasswordEvent.NavigationToChangePw -> findNavController().toChangePw(it.token, it.email)
                }
            }
        }
    }

    private fun NavController.toChangePw(token : String, email : String){
        val action = FindPasswordFragmentDirections.actionFindPasswordFragmentToChangePasswordFragment(token, email)
        navigate(action)
    }

}