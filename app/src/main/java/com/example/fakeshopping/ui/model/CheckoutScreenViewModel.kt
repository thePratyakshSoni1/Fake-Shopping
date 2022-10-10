package com.example.fakeshopping.ui.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.utils.PaymentOptionId
import com.example.fakeshopping.utils.extractIntListStringToIntList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(private val shopRepo: TestDataRepo, private val userRepo: UserRepository ) :ViewModel(){

    private val _itemsToBuy = mutableStateMapOf<Int, Int>()
    val itemsToBuy get() = _itemsToBuy as Map<Int, Int>

    private val _cartItems = mutableStateMapOf<Int, Int>()
    val cartItems get() = _cartItems as Map<Int, Int>

    private var _currentUserId:String = ""
    private val currentUserId get() = _currentUserId

    private val _discount = mutableStateOf(0f)
    val discount get() = _discount as State<Float>

    private val _totalCost = mutableStateOf(0f)
    val totalCost get() = _totalCost as State<Float>

    private val _itemsCost = mutableStateOf(0f)
    val itemsCost get() = _itemsCost as State<Float>

    private val _deliveryCharge = mutableStateOf(0f)
    val deliveryCharge get() = _deliveryCharge as State<Float>

    private val _tax = mutableStateOf(0f)
    val tax get() = _tax as State<Float>

    private val _paymentMethod = mutableStateOf(PaymentOptionId.OPTION_CARD)
    val paymentMethod get() = _paymentMethod as State<PaymentOptionId>

    fun setCurrentUser(userId:String, selectedProductsId:String, selectedProductsQuantity:String){

        _currentUserId = userId
        viewModelScope.launch{
            _itemsToBuy.clear()

            val tempItemsToBuyList = extractIntListStringToIntList(selectedProductsId)
            val tempItemsToBuyQuantityList = extractIntListStringToIntList(selectedProductsQuantity)

            Log.d("ITEMS_TO_BUY_LIST", tempItemsToBuyList.toString())
            Log.d("ITEMS_QUANTITY_LIST", tempItemsToBuyQuantityList.toString())

            for( index in tempItemsToBuyList.indices){
                _itemsToBuy[tempItemsToBuyList[index]] = tempItemsToBuyQuantityList[index]
            }
            reCalculateTotalCost()
        }

    }

    fun changeCurrentPaymentMethod(newMethodId:PaymentOptionId){
        _paymentMethod.value = newMethodId
    }

    private suspend fun reCalculateTotalCost(){

        _itemsCost.value = 0f
        _totalCost.value = 0f
        _deliveryCharge.value = 0f
        _tax.value = 0f

        viewModelScope.launch {
            for( items in itemsToBuy){

                val tempItemCost = shopRepo.getProductbyId(items.key).price.toFloat()
                _itemsCost.value +=  tempItemCost * items.value
                _tax.value += (tempItemCost * items.value) * 0.03f
                _deliveryCharge.value += if( tempItemCost > 30f) 5f else 8f

            }

            _totalCost.value = (itemsCost.value + tax.value + deliveryCharge.value) - discount.value
        }

    }




}