package com.example.fakeshopping.ui.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ShoppingCartScreenViewModel @Inject constructor(private val userRepo: UserRepository, private val shopRepo: TestDataRepo): ViewModel() {

    private lateinit var _currentUserId:String

    private val _selectedProducts: SnapshotStateMap<Int, Int> = mutableStateMapOf()
    val selectedProducts = _selectedProducts as Map<Int, Int>

    private val _isSelectionMode =  mutableStateOf(false)
    val isSelectionMode =  _isSelectionMode as State<Boolean>

    private val _cartItems = mutableStateMapOf<Int,Int>()
    val cartItems get() = _cartItems as Map<Int,Int>


    fun initalizeViewModel(currentUserId:String){

        _currentUserId = currentUserId
        viewModelScope.launch {
            _cartItems.clear()
            _cartItems.putAll(userRepo.getUserCartItems(currentUserId.toLong()))
        }

    }

    fun getProductById(productId:Int):ShopApiProductsResponse{
        var requiredProduct:ShopApiProductsResponse
        runBlocking {
            requiredProduct = shopRepo.getProductbyId(productId)
        }
        return requiredProduct
    }


}