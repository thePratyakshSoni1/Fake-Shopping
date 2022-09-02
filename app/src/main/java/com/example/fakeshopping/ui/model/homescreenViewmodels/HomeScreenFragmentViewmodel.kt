package com.example.fakeshopping.ui.model.homescreenViewmodels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenFragmentViewmodel @Inject constructor(private val repository: TestDataRepo, private val userRepo:UserRepository) : ViewModel() {

    override fun onCleared() {

        Log.d("MAIN_FRAG VIEWMODEL","Destroying")
        super.onCleared()
    }

    private val _products = mutableStateListOf<ShopApiProductsResponse>()
    private val _currentUserName = mutableStateOf<String>("")
    private val _categories = mutableStateListOf<String>()
    private val _bannerResources = mutableStateMapOf<String, Int>()
    private var _currentUserId:String= ""
    private val _userFavs = mutableStateListOf<Int>()
    val userFavs get() = _userFavs as List<Int>

    val products get() = _products
    val currentUserName get() = _currentUserName as State<String>
    val currentUserId get() = _currentUserId
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

    fun setUserId(id:String){
        _currentUserId = id
        _currentUserName.value = getCurrentUserName()
        viewModelScope.launch {

            val tempUser= userRepo.getUserByPhone(currentUserId.toLong())!!
            _currentUserName.value = "${tempUser.userFirstName} ${tempUser.userLastName}"

            _userFavs.clear()
            _userFavs.addAll(tempUser.favourites)
        }
    }

    fun getCurrentUserName(): String{
        val user:Users
        runBlocking {
            user = userRepo.getUserByPhone(currentUserId.toLong())!!
        }
        return "${user.userFirstName} ${user.userLastName}"

    }

    fun addProductToFavourites(productId:Int){

        viewModelScope.launch {
            val user = userRepo.getUserByPhone(currentUserId.toLong())!!
            user.favourites.add(productId)
            userRepo.addUser(user)

            _userFavs.clear()
            _userFavs.addAll(userRepo.getUserByPhone(currentUserId.toLong())!!.favourites)
        }

    }

    fun removeFromFavourites(productId:Int){

        viewModelScope.launch {
            val user = userRepo.getUserByPhone(currentUserId.toLong())!!
            user.favourites.remove(productId)
            userRepo.addUser(user)

            _userFavs.clear()
            _userFavs.addAll(userRepo.getUserByPhone(currentUserId.toLong())!!.favourites)
        }

    }


}