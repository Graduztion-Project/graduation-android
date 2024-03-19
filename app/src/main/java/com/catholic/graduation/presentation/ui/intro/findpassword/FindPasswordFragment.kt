package com.catholic.graduation.presentation.ui.intro.findpassword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catholic.graduation.R

class FindPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = FindPasswordFragment()
    }

    private lateinit var viewModel: FindPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FindPasswordViewModel::class.java)
        // TODO: Use the ViewModel
    }

}