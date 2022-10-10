package com.example.fakeshopping.ui.model.payment_viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razorpay.PaymentMethodsCallback
import com.razorpay.Razorpay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class NetbankingFragmentViewModel @Inject constructor(): ViewModel() {

    private lateinit var _razorpay: Razorpay
    val razorpay get() = _razorpay

    private var _amountToPay = 0F
    val amountToPay get() = _amountToPay

    private var  banksKeys: MutableList<String> = mutableListOf()

    private var _banksNameList = mutableStateListOf<String>()
    val banksNameList get() = _banksNameList as List<String>

    private var _payload = JSONObject()
    val payload get() = _payload

    private var _currentUserPhone:Long = 0L

    private lateinit var bankListJson: JSONObject

    fun initRaorpayForNetbanking(razorpayInst:Razorpay, amount:Float, currentUserId:Long ){
        _razorpay = razorpayInst
        _currentUserPhone = currentUserId
        _amountToPay = amount

        viewModelScope.launch {
            _payload.put("currency","INR")
            _payload.put("email","anymailadress@gmail.com")
            _payload.put("contact","$currentUserId")
            _payload.put("amount","${(amount * 100).toInt()}")

            Log.d("AMOUNT PAYLOAD: ",payload.toString(2))
        }

        viewModelScope.launch {
            razorpay.getPaymentMethods(
                object : PaymentMethodsCallback{
                    override fun onPaymentMethodsReceived(p0: String?) {
                        p0.let {
                            bankListJson = JSONObject(it!!).getJSONObject("netbanking")
                            Log.d("PaymentMethod RESP","$p0")

                            for(item in bankListJson.keys()){
                                banksKeys.add(item)
                                _banksNameList.add(bankListJson.getString(item))
                            }

                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }

                }
            )
        }


    }

    fun onBankChange(bankName:String){
        _payload.put("method", "netbanking")
        Log.d("SELECTED BANK", banksKeys[(banksNameList.toSet().indexOf(bankName))])
        _payload.put("bank", banksKeys[(banksNameList.toSet().indexOf(bankName))])
    }



}