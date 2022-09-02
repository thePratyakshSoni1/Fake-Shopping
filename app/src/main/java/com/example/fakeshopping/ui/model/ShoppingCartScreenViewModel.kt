package com.example.fakeshopping.ui.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ShoppingCartScreenViewModel @Inject constructor(private val userRepo: UserRepository, private val shopRepo: TestDataRepo): ViewModel() {

    private lateinit var _currentUserId:String

    private val _userFavs = mutableStateListOf<Int>()
    val userFavs get() = _userFavs

    private val _isSelectionMode =  mutableStateOf(false)
    val isSelectionMode =  _isSelectionMode as State<Boolean>

    private val _cartItems = mutableStateMapOf<Int,Int>()
    val cartItems get() = _cartItems as Map<Int,Int>


    fun initalizeViewModel(currentUserId:String){

        _currentUserId = currentUserId
        viewModelScope.launch {
            _userFavs.clear()
            _cartItems.clear()
            _userFavs.addAll(userRepo.getUserFavourites(_currentUserId.toLong()))
            _cartItems.putAll(userRepo.getUserCartItems(currentUserId.toLong()))

        }

    }

    fun toggleFavourite(itemId:Int){
        viewModelScope.launch {
            if(_userFavs.contains(itemId)){
                userRepo.removeItemFromFavourites(_currentUserId.toLong(), itemId)
                _userFavs.remove(itemId)
            }else{
                userRepo.addItemToFavourites(_currentUserId.toLong(), itemId)
                _userFavs.add(itemId)
            }
        }
    }

    fun getProductById(productId:Int):ShopApiProductsResponse{
        var requiredProduct:ShopApiProductsResponse
        runBlocking {
            requiredProduct = shopRepo.getProductbyId(productId)
        }
        return requiredProduct
    }

    fun changeQuantity(inc:Boolean, productId:Int){

        if(inc){
            _cartItems[productId] = _cartItems[productId]!! + 1
        }else{

            if(_cartItems[productId] == 1){
                _cartItems.remove(productId)
            }else{
                _cartItems[productId] = _cartItems[productId]!! - 1
            }

        }

    }

}