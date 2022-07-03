package com.example.fakeshopping.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.ShopApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProductsDetailScreenViewModel @Inject constructor(val repository:ShopApiRepository): ViewModel() {

    private var _product: MutableState<ShopApiProductsResponse?> = mutableStateOf(null)
    val product get() = _product

    val currentProductPreviewSlide:MutableState<Int> = mutableStateOf(0)

    fun setProduct(productId:Int){

        viewModelScope.launch {
            _product.value = repository.getProductbyId(productId)
        }

    }

    fun setProduct(productDetails:ShopApiProductsResponse){
        _product.value = productDetails
    }

    fun getProductImages(){

    }


}