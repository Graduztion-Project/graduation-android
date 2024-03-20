package com.catholic.graduation.presentation.ui.intro.password.changepassword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catholic.graduation.R
import com.catholic.graduation.databinding.FragmentChangePasswordBinding
import com.catholic.graduation.databinding.FragmentFindPasswordBinding
import com.catholic.graduation.presentation.base.BaseFragment
import com.catholic.graduation.presentation.ui.intro.password.findpassword.FindPasswordEvent
import com.catholic.graduation.presentation.ui.intro.password.findpassword.FindPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>(R.layout.fragment_change_password) {

    private val viewModel : ChangePasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()

    }

    private fun initEventObserve() {
//        repeatOnStarted {
//            viewModel.event.collect{
//                when(it){
//                    FindPasswordEvent.NavigateToBack -> findNavController().navigateUp()
//                    is FindPasswordEvent.ShowToastMessage -> showToastMessage(it.msg)
//                    is FindPasswordEvent.NavigationToChangePw -> findNavController().toChangePw(it.token)
//                }
//            }
//        }
    }

}