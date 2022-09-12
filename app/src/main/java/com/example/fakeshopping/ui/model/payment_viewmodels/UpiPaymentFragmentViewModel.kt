package com.example.fakeshopping.ui.model.payment_viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpiPaymentFragmentViewModel @Inject constructor(): ViewModel() {

    private val _upiId = mutableStateOf("")
    val upiId get() = _upiId as State<String>

    fun onUpiIdTextChange(newvalue:String){
        _upiId.value= newvalue
    }


}