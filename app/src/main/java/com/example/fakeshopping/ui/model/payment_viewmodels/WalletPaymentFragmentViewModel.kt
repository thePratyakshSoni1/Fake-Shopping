package com.example.fakeshopping.ui.model.payment_viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razorpay.PaymentMethodsCallback
import com.razorpay.PaymentResultListener
import com.razorpay.Razorpay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class WalletPaymentFragmentViewModel @Inject constructor(): ViewModel() {

    lateinit private var _razorpay:Razorpay
    val razorpay get() = _razorpay

    private var _payload= JSONObject()
    val payload get() = _payload

    private val _currentUser = mutableStateOf(0L)
    val currentUser get() = _currentUser as State<Long>

    private val _walletsLists = mutableStateListOf<String>()
    val walletsLists get() = _walletsLists as List<String>

    fun initRazorpayAndCurrentUser( rPay:Razorpay, currentUserId:Long, amount:Float){

        _razorpay = rPay
        _currentUser.value = currentUserId

        viewModelScope.launch {
            _payload.put("currency","INR")
            _payload.put("email","anymailadress@gmail.com")
            _payload.put("contact","$currentUserId")
            _payload.put("amount","${(amount * 100).toInt()}")
            Log.d("AMOUNT PAYLOAD: ",payload.toString(2))
        }

        razorpay.getPaymentMethods( object: PaymentMethodsCallback {

                override fun onPaymentMethodsReceived(p0: String?) {

                    p0.let{
                        val walletsLsist = JSONObject(it!!).getJSONObject("wallet")
                        for( item in walletsLsist.keys()){
                            if(walletsLsist.getBoolean(item)){
                                _walletsLists.add(item)
                            }
                        }
                    }

                }

                override fun onError(p0: String?) {
                    Log.d("PaymentMethod_ERR", "GOT Error:\n $p0")
                }

            }

        )

    }



    fun onWalletChange(walletName:String){
        _payload.put("method", "wallet")
        Log.d("SELECTED BANK", "walet changed to: $walletName" )
        _payload.put("wallet", walletName)
    }

}