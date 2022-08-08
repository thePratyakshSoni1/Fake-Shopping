package com.example.fakeshopping.ui.model

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.SelectedProduct
import com.example.fakeshopping.data.ShopApiProductsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteScreenViewmodel @Inject constructor() : ViewModel(){

    private val _favouriteProducts = mutableStateMapOf<Int,ShopApiProductsResponse>()
    val favouriteProducts = _favouriteProducts as Map<Int, ShopApiProductsResponse>

    private val _selectedProducts: SnapshotStateMap<Int,Int> = mutableStateMapOf()
    val selectedProducts = _selectedProducts as Map<Int, Int>

    private val _isSelectionMode =  mutableStateOf(false)
    val isSelectionMode =  _isSelectionMode as State<Boolean>

    init {
        //get afvourite product data from databse
        for (items in 1..20) {

            _favouriteProducts[items] = ShopApiProductsResponse(
                id = (items),
                "Product Name $items is here",
                "$79",
                "all",
                "Product Desc ;)",
                "#xyz",
                ShopApiProductsResponse.ProductRating(
                    rate = 3.5f,
                    count = items
                )
            )

        }
    }

    val changeSelectionModeTo:(Boolean)->Unit = {
        _isSelectionMode.value = it
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

    fun deleteItemFromFavourite(id:Int){
        viewModelScope.launch {
            if(_selectedProducts.containsKey(id)) {
                _selectedProducts.remove(id)
            }
            _favouriteProducts.remove(id)
        }

    }


    fun onItemRemove(itemId:Int){
        viewModelScope.launch{
            delay(350L)
            _selectedProducts.remove(itemId)
        }
    }

    fun removeAllSelectedFromFavourites(){

        viewModelScope.launch {
            _selectedProducts.forEach{ item ->
                _favouriteProducts.remove(item.key)
            }
            _selectedProducts.clear()
            _isSelectionMode.value = false
        }

    }

    fun toggleFavourite(itemId:Int){
        if(_favouriteProducts.containsKey(itemId)){
            _favouriteProducts.remove(itemId)
        }
    }

    fun clearSelection(){
        _selectedProducts.clear()
        _isSelectionMode.value = false
    }

    fun changeQuantity(itemId:Int, newQuantity:Int){
        _selectedProducts[itemId] = newQuantity
    }


    fun addAllSelectedProducts(itemList:Map<Int,Int>){
        for(productId in itemList.keys){
            if(!_selectedProducts.contains(productId)){
                _selectedProducts[productId] = itemList[productId]!!
            }
        }
    }

}