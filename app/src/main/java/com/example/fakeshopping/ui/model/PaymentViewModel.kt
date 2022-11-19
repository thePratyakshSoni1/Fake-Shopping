package com.example.fakeshopping.ui.model

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.utils.OrderDeliveryStatus
import com.example.fakeshopping.utils.PaymentOptionId
import com.example.fakeshopping.utils.PaymentScreenRoutes
import com.example.fakeshopping.utils.extractIntListStringToIntList
import com.example.fakeshopping.workers.OrderPlacementWorker
import com.example.fakeshopping.workers.OrderWorkerKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor( private val userRepo: UserRepository, val application:Application) : ViewModel() {

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
            orderDeliveryStatus = OrderDeliveryStatus.STATUS_PROCESSING.value,
            orderDeliverTime = null,
            productQuantity = itemsToBuyQuantity,
            orderDeliveryAddress = "${userAddress.landmark}, ${userAddress.pincode} ${userAddress.city}, ${userAddress.state}, ${userAddress.country}",
            totalAmountPaid = amountToPay.value,
            paymentMethod = paymentMethod?.id!!,
            razorpayPaymentId= razorpayPaymentId
        )
    }


    fun updateUserCartAfterPayment(razorpayPaymentId: String){

        lateinit var tempUserOrderDetail:UserOrders
        viewModelScope.launch {
            val user = userRepo.getUserByPhone(currentUserId.toLong())!!
            tempUserOrderDetail = storeOrderDetails(razorpayPaymentId)

            user.userOrders.add(tempUserOrderDetail)
            Log.d("ORDER_DATASAVED","Strong Data: $tempUserOrderDetail")
            itemsToBuy.forEach{
                user.cartItems.remove(it)
            }
            userRepo.updateUser(user)

            val workManager = WorkManager.getInstance(application)
            workManager.beginUniqueWork(
                "FSWORKER_ORDERPLACEMENT_UNIQUEWORK",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<OrderPlacementWorker>().apply {

                    setInitialDelay(20L, TimeUnit.SECONDS)
                    Log.d("ORDERID","Adding : ${tempUserOrderDetail.orderId}")

                    setInputData(
                        Data.Builder()
                            .putAll(
                                mutableMapOf(
                                    OrderWorkerKeys.KEY_ORDEID.value to tempUserOrderDetail.orderId.toString(),
                                    OrderWorkerKeys.KEY_CURRENT_USER_ID.value to currentUserId
                                ) as Map<String, Any>
                            )
                            .build()
                    )

                }.build()

            ).enqueue()

        }


    }

}