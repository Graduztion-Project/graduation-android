package com.catholic.graduation.presentation.bindingadapters

import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.catholic.graduation.R
import com.catholic.graduation.presentation.ui.InputState

@BindingAdapter("etStateColor")
fun bindETStateColor(et: EditText, state: InputState){
    when(state){
        is InputState.Error -> {
            et.setTextColor(ContextCompat.getColor(et.context, R.color.red))
        }
        else -> {
            et.setTextColor(ContextCompat.getColor(et.context, R.color.black))
        }
    }
}