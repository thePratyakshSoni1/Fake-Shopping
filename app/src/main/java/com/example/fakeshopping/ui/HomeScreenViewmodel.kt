package com.example.fakeshopping.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.R
import com.example.fakeshopping.data.FakeShopApi
import com.example.fakeshopping.data.ShopApiCategoriesResponse
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.ShopApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewmodel @Inject constructor( private val repository:ShopApiRepository):ViewModel() {

    private val _products = mutableStateListOf<ShopApiProductsResponse>()
    val products get() = _products
    private val _categories = mutableStateListOf<String>()
    val categories get() = _categories

    init {
        refreshProducts()
        refreshCategories()
    }

    private fun refreshProducts(){
        viewModelScope.launch {
            _products.addAll(repository.getallProducts())
        }
    }

    private fun refreshCategories(){
        viewModelScope.launch {
            _categories.addAll(repository.getAllCategories())
        }
    }


    fun generateBannerSlidesResouuce():Map<String,Int>{

        val bannerResource = mutableMapOf<String,Int>()
        categories.forEach {
            when(it){
                "electronics" -> bannerResource.put(it , R.drawable.electronics_category_display)
                "jewelery" -> bannerResource.put(it , R.drawable.jwellery_category_display)
                "men's clothing" -> bannerResource.put(it , R.drawable.mensclothes_category_display)
                "women's clothing" -> bannerResource.put(it , R.drawable.womenclothes_category_display)
            }
        }

        return bannerResource

    }

}