package com.example.fakeshopping.ui.model

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.utils.extractIntListStringToIntList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor( private val userRepo: UserRepository) : ViewModel() {

    private var _currentUserId:String = ""
    private val currentUserId get() = _currentUserId

    private var _itemsToBuy = mutableStateListOf<Int>()
    private val itemsToBuy get() = _itemsToBuy

    private var _itemsToBuyQuantity = mutableStateListOf<Int>()
    private val itemsToBuyQuantity get() = _itemsToBuyQuantity

    private var _isPaymentSuccess = mutableStateOf(false)
    private var _isPaymentDialogVisible by mutableStateOf(false)

    val isPaymentSuccess get() = _isPaymentSuccess
    val isPaymentDialogVisible get() = _isPaymentDialogVisible


    fun initViewModel(currentUserId:Long, itemsToBuyListString:String, itemsToBuyQuantityListString:String ){
        _currentUserId = currentUserId.toString()

        viewModelScope.launch {
            _itemsToBuy.clear()
            val tempItemsToBuy = extractIntListStringToIntList(itemsToBuyListString)
            _itemsToBuy.addAll(tempItemsToBuy)
        }

        viewModelScope.launch {
            _itemsToBuyQuantity.clear()
            val tempItemsToBuyQuantity = extractIntListStringToIntList(itemsToBuyQuantityListString)
            _itemsToBuyQuantity.addAll(tempItemsToBuyQuantity)
        }

    }

    fun setPaymentSuccessStatus(paymentSucceed:Boolean){
        isPaymentSuccess.value = paymentSucceed
    }

    fun setPaymentDialogVisibility(setToVisible:Boolean){
        _isPaymentDialogVisible = setToVisible
    }

    fun storeOrderDetails():UserOrders{
        var currentMills = System.currentTimeMillis().toString()
        var millsLastDigits = currentMills.subSequence(currentMills.length-5,currentMills.length-1)
        var simpleStrFormat = SimpleDateFormat("ddMMyy", Locale.US)
        var date = Calendar.getInstance().time
        var orderId = "${currentUserId.subSequence(6,9)}${millsLastDigits}${simpleStrFormat.format(date)}"

        var order= UserOrders(
            orderId = orderId.toLong(),
            productId = itemsToBuy,
            orderDateTime = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date),
            orderDelivered = null,
            orderDeliverTime = null,
            productQuantity = itemsToBuyQuantity,
            orderDeliveryAddress = "Azad Nagar, 225001 Barabanki, Uttar Pradesh, India"
        )
        return order
    }


    fun updateUserCartAfterPayment(){

        viewModelScope.launch {
            val user = userRepo.getUserByPhone(currentUserId.toLong())!!
            val tempUserOrderDetail = storeOrderDetails()
            user.userOrders.add(tempUserOrderDetail)
            Log.d("ORDER_DATASAVED","Strong Data: $tempUserOrderDetail")
            itemsToBuy.forEach{
                user.cartItems.remove(it)
            }
            userRepo.updateUser(user)
        }

    }

}