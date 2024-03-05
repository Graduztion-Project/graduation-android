package com.catholic.graduation.presentation.bindingadapters

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.catholic.graduation.R
import com.catholic.graduation.presentation.ui.InputState

@BindingAdapter("helperMessage")
fun bindHelperMessage(tv: TextView, state: InputState) {
    when (state) {
        is InputState.Empty ->
            tv.visibility = View.INVISIBLE

        is InputState.Success -> {
            tv.visibility = View.VISIBLE
            tv.text = state.msg
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.black))
        }

        is InputState.Error -> {
            tv.visibility = View.VISIBLE
            tv.text = state.msg
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.red))
        }
    }
}