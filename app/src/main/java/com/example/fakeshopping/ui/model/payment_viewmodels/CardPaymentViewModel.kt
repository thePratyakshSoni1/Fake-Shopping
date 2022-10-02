package com.example.fakeshopping.ui.model.payment_viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CardPaymentViewModel @Inject constructor(private val userRepo:UserRepository): ViewModel() {

    private val _cardHolderName = mutableStateOf("")
    private val _cardNumber = mutableStateOf("")
    private val _cardExpiry = mutableStateOf("")
    private val _cardCvv = mutableStateOf("")
    private val _isCvvVisible = mutableStateOf(false)
    private var _currentUserPhone:Long = 0L
    val currentUserPhone:Long get() = _currentUserPhone

    val cardHolderName get() =_cardHolderName as State<String>
    val cardNumber get() = _cardNumber as State<String>
    val cardExpiry get() = _cardExpiry as State<String>
    val cardCvv get() = _cardCvv as State<String>
    val isCvvVisible get() = _isCvvVisible as State<Boolean>
    val payload = mutableStateOf<JSONObject?>(null)

    fun initRazorpayPayloadAndAmout(amountToBePaid:Float, currentUserId:Long){

        _currentUserPhone = currentUserId

        try {
            payload.value = JSONObject("{currency: 'INR'}")
            payload.value!!.put("amount", (amountToBePaid* 100).toInt().toString()) // (10Rs) in paise  i.e. 1Rs = 100P
            payload.value!!.put("contact", currentUserId)
            payload.value!!.put("email", "pratyakshsoni2004.test@gmil.com")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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