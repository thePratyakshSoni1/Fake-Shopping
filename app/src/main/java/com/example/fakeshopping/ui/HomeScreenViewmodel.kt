package com.example.fakeshopping.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.TestDataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewmodel @Inject constructor(private val repository: ShopApiRepository) :
    ViewModel() {

    private val _products = mutableStateListOf<ShopApiProductsResponse>()
    private val _categories = mutableStateListOf<String>()
    private val _bannerResources = mutableStateMapOf<String, Int>()

    val products get() = _products
    val categories get() = _categories
    val bannerResources get() = _bannerResources

    val searchBoxText = mutableStateOf("")
    val selectedCategory = mutableStateOf("All")

    val userInteractedWithBanners = mutableStateOf(false)

    fun refreshProducts() {
        viewModelScope.launch {
            _products.addAll(repository.getallProducts())
            Log.i("API", "Products Updated")
        }
    }

    fun refreshCategories() {
        viewModelScope.launch {
            _categories.clear()
            _categories.addAll(repository.getAllCategories())
            generateBannerSlidesResouuce()
            Log.i("API", "Categories Updated")
        }
    }


    fun generateBannerSlidesResouuce() {

        val bannerResource = mutableMapOf<String, Int>()
        Log.i("API", "Generating Banner Resources")
        categories.forEach {
            when (it) {
                "electronics" -> bannerResource.put(it, R.drawable.electronics_category_display)
                "jewelery" -> bannerResource.put(it, R.drawable.jwellery_category_display)
                "men's clothing" -> bannerResource.put(it, R.drawable.mensclothes_category_display)
                "women's clothing" -> bannerResource.put(
                    it,
                    R.drawable.womenclothes_category_display
                )
            }
        }

        _bannerResources.putAll(bannerResource)

    }

    fun changeCategory(category: String) {
        Log.d("CATEGORY_CHANGE","Changing Category")
        viewModelScope.launch {
            selectedCategory.value = category
            _products.clear()
            Log.d("CATEGORY_CHANGE","CATEGORY: ${selectedCategory.value}\n\t\t${products.toList()}")
            _products.addAll(repository.getProductFromCategory(category))
            Log.d("CATEGORY_CHANGE","CATEGORY: ${selectedCategory.value}\n\t\t${products.toList()}")
        }
    }

}