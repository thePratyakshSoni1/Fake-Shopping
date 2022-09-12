package com.example.fakeshopping.ui.model.payment_viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fakeshopping.data.repository.TestDataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardPaymentViewModel @Inject constructor(private val shopRepo: TestDataRepo): ViewModel() {

    private val _cardHolderName = mutableStateOf("")
    private val _cardNumber = mutableStateOf("")
    private val _cardExpiry = mutableStateOf("")
    private val _cardCvv = mutableStateOf("")
    private val _isCvvVisible = mutableStateOf(false)

    val cardHolderName get() =_cardHolderName as State<String>
    val cardNumber get() = _cardNumber as State<String>
    val cardExpiry get() = _cardExpiry as State<String>
    val cardCvv get() = _cardCvv as State<String>
    val isCvvVisible get() = _isCvvVisible as State<Boolean>

    fun onCardHolderNameTextChange(newValue:String){
        _cardHolderName.value = newValue
    }

    fun onCardExpiryTextChange(newValue:String){
        _cardExpiry.value = newValue
    }

    fun onCardNumberTextChange(newValue:String){
        _cardNumber.value = newValue
    }

    fun onCardCvvTextChange(newValue:String){
        _cardCvv.value = newValue
    }

    fun toggleCvvVisibility(){
        _isCvvVisible.value = !_isCvvVisible.value
    }

}