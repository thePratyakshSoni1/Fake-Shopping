package com.example.fakeshopping.ui.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(private val shopRepo: TestDataRepo, private val userRepo: UserRepository ) :ViewModel(){

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

    fun setCurrentUser(userId:String){

        _currentUserId = userId
        viewModelScope.launch{
            _cartItems.putAll(userRepo.getUserCartItems(userId.toLong()))
            reCalculateTotalCost()
        }

    }

    private suspend fun reCalculateTotalCost(){

        _itemsCost.value = 0f
        _totalCost.value = 0f
        _deliveryCharge.value = 0f
        _tax.value = 0f

        for( items in cartItems){

            val tempItemCost = shopRepo.getProductbyId(items.key).price.toFloat()
            _itemsCost.value +=  tempItemCost * items.value
            _tax.value += (tempItemCost * items.value) * 0.03f
            _deliveryCharge.value += if( tempItemCost > 30f) 5f else 8f

        }

        _totalCost.value = (itemsCost.value + tax.value + deliveryCharge.value) - discount.value

    }


}