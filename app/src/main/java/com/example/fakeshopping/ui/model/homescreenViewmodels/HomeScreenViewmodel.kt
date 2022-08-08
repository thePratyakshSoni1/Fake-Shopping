package com.example.fakeshopping.ui.model.homescreenViewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.TestDataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewmodel @Inject constructor(private val repository: TestDataRepo) : ViewModel() {

    override fun onCleared() {

        Log.d("MAIN_FRAG VIEWMODEL","Destroying")
        super.onCleared()
    }

    private val _products = mutableStateListOf<ShopApiProductsResponse>()
    private val _categories = mutableStateListOf<String>()
    private val _bannerResources = mutableStateMapOf<String, Int>()

    val products get() = _products
    val categories get() = _categories
    val bannerResources get() = _bannerResources

    val selectedCategory = mutableStateOf("All")

    val userInteractedWithBanners = mutableStateOf(false)

    init{

        viewModelScope.launch{
            val refreshingCategories = async { refreshCategories() }
            refreshingCategories.await()
            generateBannerSlidesResouuce()
        }

        Log.d("MAIN_FRAG VIEWMODEL", "Initialized")

    }

    fun refreshProducts() {
        viewModelScope.launch {
            _products.addAll(repository.getallProducts())
            Log.i("API", "Products Updated")
        }
    }

    private suspend fun refreshCategories() {
            _categories.clear()
            _categories.addAll(repository.getAllCategories())
            Log.i("API", "Categories Updated")
    }

    private fun generateBannerSlidesResouuce() {

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
        viewModelScope.launch{
            Log.d("CATEGORY_CHANGE", "Changing Category")
            selectedCategory.value = category
            _products.clear()
            Log.d(
                "CATEGORY_CHANGE",
                "CATEGORY: ${selectedCategory.value}\n\t\t${products.toList()}"
            )
            _products.addAll(repository.getProductFromCategory(category))
            Log.d(
                "CATEGORY_CHANGE",
                "CATEGORY: ${selectedCategory.value}\n\t\t${products.toList()}"
            )
        }
    }



}