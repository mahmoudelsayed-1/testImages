package com.example.testimages.common

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:Error")

fun bindError(editText:EditText , errorMsg :String? ){

    editText.error = errorMsg

}

@BindingAdapter("app:Errorr")

fun bindError(txtView:TextView , errorMsg :String? ){

    txtView.error = errorMsg

}