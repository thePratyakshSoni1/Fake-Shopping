package com.example.fakeshopping.ui.model

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.utils.PaymentOptionId
import com.example.fakeshopping.utils.PaymentScreenRoutes
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

    private var _amountToPay = mutableStateOf(0F)
    val amountToPay get() = _amountToPay as State<Float>

    private var _itemsToBuy = mutableStateListOf<Int>()
    private val itemsToBuy get() = _itemsToBuy

    private var _itemsToBuyQuantity = mutableStateListOf<Int>()
    private val itemsToBuyQuantity get() = _itemsToBuyQuantity

    private var _isPaymentSuccess = mutableStateOf(false)
    private var _isPaymentDialogVisible by mutableStateOf(false)

    val isPaymentSuccess get() = _isPaymentSuccess
    val isPaymentDialogVisible get() = _isPaymentDialogVisible

    var paymentMethod: PaymentOptionId? = null


    fun initViewModel(currentUserId:Long, itemsToBuyListString:String, itemsToBuyQuantityListString:String, amountToPay:Float, methodpath:String ){
        _currentUserId = currentUserId.toString()

        viewModelScope.launch {
            _amountToPay.value = amountToPay
            _itemsToBuy.clear()
            val tempItemsToBuy = extractIntListStringToIntList(itemsToBuyListString)
            _itemsToBuy.addAll(tempItemsToBuy)
        }

        viewModelScope.launch {
            _itemsToBuyQuantity.clear()
            val tempItemsToBuyQuantity = extractIntListStringToIntList(itemsToBuyQuantityListString)
            _itemsToBuyQuantity.addAll(tempItemsToBuyQuantity)
        }

        viewModelScope.launch {
            paymentMethod = when(methodpath){
                PaymentScreenRoutes.cardFragment -> PaymentOptionId.OPTION_CARD
                PaymentScreenRoutes.netBankingFragment -> PaymentOptionId.OPTOIN_NETBANKING
                PaymentScreenRoutes.upiFragment -> PaymentOptionId.OPTION_UPI
                PaymentScreenRoutes.walletFragment -> PaymentOptionId.OPTOIN_WALLET
                else -> PaymentOptionId.OPTION_POD
            }
        }

    }

    fun setPaymentSuccessStatus(paymentSucceed:Boolean){
        isPaymentSuccess.value = paymentSucceed
    }

    fun setPaymentDialogVisibility(setToVisible:Boolean){
        _isPaymentDialogVisible = setToVisible
    }

    fun updatePaymentMethod(methodId:PaymentOptionId){
        paymentMethod = methodId
    }

    suspend fun storeOrderDetails(razorpayPaymentId:String): UserOrders {
        val currentMills = System.currentTimeMillis().toString()
        val millsLastDigits = currentMills.subSequence(currentMills.length - 5, currentMills.length - 1)
        val simpleStrFormat = SimpleDateFormat("ddMMyy", Locale.US)
        val date = Calendar.getInstance().time
        val orderId =
            "${currentUserId.subSequence(6, 9)}${millsLastDigits}${simpleStrFormat.format(date)}"
        val userAddress = userRepo.getUserAddress(currentUserId.toLong())


        return UserOrders(
            orderId = orderId.toLong(),
            productId = itemsToBuy,
            orderDateTime = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date),
            orderDelivered = null,
            orderDeliverTime = null,
            productQuantity = itemsToBuyQuantity,
            orderDeliveryAddress = "${userAddress.landmark}, ${userAddress.pincode} ${userAddress.city}, ${userAddress.state}, ${userAddress.country}",
            totalAmountPaid = amountToPay.value,
            paymentMethod = paymentMethod?.id!!,
            razorpayPaymentId= razorpayPaymentId
        )
    }


    fun updateUserCartAfterPayment(razorpayPaymentId: String){

        viewModelScope.launch {
            val user = userRepo.getUserByPhone(currentUserId.toLong())!!
            val tempUserOrderDetail = storeOrderDetails(razorpayPaymentId)
            user.userOrders.add(tempUserOrderDetail)
            Log.d("ORDER_DATASAVED","Strong Data: $tempUserOrderDetail")
            itemsToBuy.forEach{
                user.cartItems.remove(it)
            }
            userRepo.updateUser(user)
        }

    }

}