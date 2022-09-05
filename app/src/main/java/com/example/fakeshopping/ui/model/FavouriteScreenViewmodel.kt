package com.example.fakeshopping.ui.model

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.SelectedProduct
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteScreenViewmodel @Inject constructor( private val userRepo:UserRepository, private val shopRepo:TestDataRepo ) : ViewModel(){

    private lateinit var _currentUserId:String

    private val _favouriteProducts = mutableStateMapOf<Int,ShopApiProductsResponse>()
    val favouriteProducts = _favouriteProducts as Map<Int, ShopApiProductsResponse>

    private val _selectedProducts: SnapshotStateMap<Int,Int> = mutableStateMapOf()
    val selectedProducts = _selectedProducts as Map<Int, Int>

    private val _isSelectionMode =  mutableStateOf(false)
    val isSelectionMode =  _isSelectionMode as State<Boolean>

    val changeSelectionModeTo:(Boolean)->Unit = {
        _isSelectionMode.value = it
    }


    fun setCurrentUserAndFavs(id:String){

        _currentUserId = id
        viewModelScope.launch {
            for (items in userRepo.getUserFavourites(_currentUserId.toLong()) ){
                _favouriteProducts[items] = shopRepo.getProductbyId(items)
            }
        }

    }

    fun addNewSelectedItem(itemId:Int, quantity:Int= 1):Boolean{

        return if(!_selectedProducts.contains(itemId)) {
            _selectedProducts[itemId] = quantity
            Log.d("Data","List = ${_favouriteProducts.keys.toList()}")
            true
        }else{
            Log.d("Data","List = ${_favouriteProducts.keys.toList()}")
            false
        }

    }

//    fun deleteItemFromFavourite(id:Int){
//        viewModelScope.launch {
//            if(_selectedProducts.containsKey(id)) {
//                _selectedProducts.remove(id)
//            }
//            _favouriteProducts.remove(id)
//        }
//
//    }


    fun onItemRemove(itemId:Int){
        viewModelScope.launch{
            delay(350L)
            _selectedProducts.remove(itemId)
        }
    }

    fun removeAllSelectedFromFavourites(){

        viewModelScope.launch {
            val tempUser = userRepo.getUserByPhone(_currentUserId.toLong())!!
            _selectedProducts.forEach{ item ->
                tempUser.favourites.remove(item.key)
                _favouriteProducts.remove(item.key)
            }
            _selectedProducts.clear()
            _isSelectionMode.value = false
        }

    }

    fun toggleFavourite(itemId:Int){
        viewModelScope.launch {
            if(_favouriteProducts.contains(itemId)){
                userRepo.removeItemFromFavourites(_currentUserId.toLong(), itemId)
                _favouriteProducts.remove(itemId)
            }else{
                userRepo.addItemToFavourites(_currentUserId.toLong(), itemId)
                _favouriteProducts[itemId] = shopRepo.getProductbyId(itemId)
            }
            val tempFavs = mutableMapOf<Int,ShopApiProductsResponse>()
            for(items in userRepo.getUserFavourites(_currentUserId.toLong())){
                tempFavs[items] = shopRepo.getProductbyId(items)
                Log.d("FAV_ACTION", "Refreshing actionId: $itemId")
            }
            _favouriteProducts.clear()
            _favouriteProducts.putAll(tempFavs)
        }
    }

    fun clearSelection(){
        _selectedProducts.clear()
        _isSelectionMode.value = false
    }

    fun changeQuantity(inc:Boolean, itemId:Int){
        if(inc){
            _selectedProducts[itemId] = selectedProducts[itemId]!! + 1
        }else{
            if(_selectedProducts[itemId]!! > 1){
                _selectedProducts[itemId] = selectedProducts[itemId]!! - 1
            }
        }
    }


    fun moveToCart(){

        viewModelScope.launch {
            val user = userRepo.getUserByPhone(_currentUserId.toLong())!!
            for(items in selectedProducts){

                if(user.cartItems.contains(items.key)){
                    val updatedQuantity = user.cartItems[items.key]!! + items.value
                    user.cartItems[items.key] = updatedQuantity
                }else{
                    user.cartItems[items.key] = items.value
                }

            }
            userRepo.updateUser(user)
            _selectedProducts.clear()
            _isSelectionMode.value = false
        }

    }

}