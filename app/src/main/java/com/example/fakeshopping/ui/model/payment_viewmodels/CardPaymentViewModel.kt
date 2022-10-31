package com.example.fakeshopping.ui.model.payment_viewmodels

import android.util.Log
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

    fun isHolderNameValid():Boolean{
        return !cardHolderName.value.contains( Regex("[^A-Za-z]")) && cardHolderName.value.isNotEmpty()
    }

    fun isCardNumberValid():Boolean{
        Log.d("REGEX TEST"," Contains Non-Digit: ${ cardNumber.value.contains( Regex("[^0-9] "))}")
        return cardNumber.value.length == 14 && !cardNumber.value.contains( Regex("[^0-9]"))
    }

    fun isCardExpiryValid():Boolean{
        return  cardExpiry.value.contains("/") &&
                cardExpiry.value.split("/")[1].length == 2 &&
                cardExpiry.value.split("/")[0].isNotEmpty() &&
                cardExpiry.value.split("/")[0].contains(Regex("[^0-9]")) &&
                cardExpiry.value.split("/")[1].contains(Regex("[^0-9]"))

    }

    fun isValidCVV():Boolean{
        return cardCvv.value.contains(Regex("[^0-9]")) && cardCvv.value.length == 3

    }

    fun preparePayloadForRequest(){
        try {
            payload.value?.put("method", "card")
            payload.value?.put("card[name]", cardHolderName.value)
            payload.value?.put("card[number]", cardNumber.value)
            payload.value?.put("card[expiry_month]", cardExpiry.value.split("/")[0])
            payload.value?.put("card[expiry_year]",  cardExpiry.value.split("/")[1])
            payload.value?.put("card[cvv]",  cardCvv.value)
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