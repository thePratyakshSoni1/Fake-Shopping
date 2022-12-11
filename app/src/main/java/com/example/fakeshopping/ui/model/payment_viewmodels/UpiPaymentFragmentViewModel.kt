package com.example.fakeshopping.ui.model.payment_viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.razorpay.Razorpay
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class UpiPaymentFragmentViewModel @Inject constructor(): ViewModel() {

    private val _upiId = mutableStateOf("")
    val upiId get() = _upiId as State<String>

    private val _currentUserId = mutableStateOf(0L)
    val currentuserId get() = _currentUserId as State<Long>

    private var amountToPay:Float = 0F

    val payload = JSONObject()

    private lateinit var razorpay:Razorpay

    fun onUpiIdTextChange(newvalue:String){
        _upiId.value= newvalue
    }

    fun initViewModelWithuserandRazorpay(rPay:Razorpay, currentuser:Long, amount:Float ){

        razorpay = rPay
        _currentUserId.value = currentuser
        amountToPay =  amount

        prepareBasePaylod(amount = amountToPay)

    }

    fun onIntentFlowClick(){

        val prefferedUpis = JSONArray()
        val otherUpis = JSONArray()

        prefferedUpis.put("com.google.android.apps.nbu.paisa.user")
        prefferedUpis.put("in.org.npci.upiapp")
        prefferedUpis.put("com.phonepe.app")
        prefferedUpis.put("net.one97.paytm")
        prefferedUpis.put("in.amazon.mShop.android.shopping")

        otherUpis.put("com.sbi.upi")
        otherUpis.put("com.fss.pnbpsp")

        payload.put("method","upi")
        payload.put("_[flow]","intent")
        payload.put("preferred_apps_order",prefferedUpis)
        payload.put("other_apps_order",otherUpis)
    }

    fun onIntentCollectClick(){

        payload.put("method","upi")
        payload.put("vpa",upiId.value)

    }

    private fun prepareBasePaylod(amount:Float){

        payload.put("currency","INR")
        payload.put("email","anymailadress@gmail.com")
        payload.put("contact","${currentuserId.value}")
        payload.put("amount","${(amount * 100).toInt()}")

    }


}