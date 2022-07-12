package com.example.fakeshopping.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.TestDataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsDetailScreenViewModel @Inject constructor(val repository: TestDataRepo) :
    ViewModel() {

    private var _product: MutableState<ShopApiProductsResponse?> = mutableStateOf(null)
    val product get() = _product

    private var _relevantProducts: SnapshotStateList<ShopApiProductsResponse> = mutableStateListOf()
    val relevantproduct get() = _relevantProducts

    private var _otherProducts: SnapshotStateList<ShopApiProductsResponse> = mutableStateListOf()
    val otherPproducts get() = _otherProducts


    val currentProductPreviewSlide: MutableState<Int> = mutableStateOf(0)

    fun setProduct(productId: Int) {

        viewModelScope.launch {

            _product.value = repository.getProductbyId(productId)
            _relevantProducts.addAll(getRandomProducts())
            _otherProducts.addAll(getRelevantProducts())

            Log.d("PROD_VIEWMODEL","Rel: ${relevantproduct.toList()}\n\nOther: ${otherPproducts.toList()}")

        }

    }

    fun setProduct(productDetails: ShopApiProductsResponse) {
        _product.value = productDetails
    }

    suspend fun getRelevantProducts():List<ShopApiProductsResponse> {

        val relevantProducts = mutableListOf<ShopApiProductsResponse>()
        val allProducts = repository.getProductFromCategory(product.value!!.category)
        repeat(6) {
            relevantProducts.add(allProducts.random())
        }

        Log.d("PROD_VIEWMODEL","returning: ${relevantproduct.toList()}")
        return relevantProducts
    }

    suspend fun getRandomProducts():List<ShopApiProductsResponse> {

        val randomProducts = mutableListOf<ShopApiProductsResponse>()
        val allProducts = repository.getallProducts()
        repeat(6){
            randomProducts.add(allProducts.random())
        }
        return randomProducts
    }


}