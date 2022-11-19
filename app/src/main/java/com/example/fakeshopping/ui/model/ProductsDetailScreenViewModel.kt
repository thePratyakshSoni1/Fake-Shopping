package com.example.fakeshopping.ui.model

import android.app.Application
import android.security.keystore.StrongBoxUnavailableException
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.workers.OrderPlacementWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProductsDetailScreenViewModel @Inject constructor(private val shopRepo: TestDataRepo, private val userRepo:UserRepository, val application: Application) : ViewModel() {

    private var _product: MutableState<ShopApiProductsResponse?> = mutableStateOf(null)
    val product get() = _product

    private var _currentUserId:String = ""
    val currentUserId get() = _currentUserId

    private var _relevantProducts: SnapshotStateList<ShopApiProductsResponse> = mutableStateListOf()
    val relevantproduct get() = _relevantProducts

    private var _userFavs: SnapshotStateList<Int> = mutableStateListOf()
    val userFavs get() = _userFavs as List<Int>

    private var _otherProducts: SnapshotStateList<ShopApiProductsResponse> = mutableStateListOf()
    val otherPproducts get() = _otherProducts

    private val _isFavouriteProduct = mutableStateOf<Boolean?>( null )
    val isFavouriteProduct get() = _isFavouriteProduct as State<Boolean?>

    val currentProductPreviewSlide: MutableState<Int> = mutableStateOf(0)

    fun setProductAndUserId(productId: Int, userId:String) {

        _currentUserId = userId
        viewModelScope.launch {
            _product.value = shopRepo.getProductbyId(productId)
            _relevantProducts.addAll(getRelevantProducts())
            _otherProducts.addAll(getRandomProducts())

            _userFavs.clear()
            _userFavs.addAll(userRepo.getUserFavourites(currentUserId.toLong()))
            updateCurrentProductFavStatus()
            Log.d("PROD_VIEWMODEL","Rel: ${relevantproduct.toList()}\n\nOther: ${otherPproducts.toList()}")

        }

    }

    private fun updateCurrentProductFavStatus(){
        _isFavouriteProduct.value = _userFavs.contains(product.value!!.id)
    }

    fun addProductToFavourites(productId:Int){

        _userFavs.add(productId)
        updateCurrentProductFavStatus()
        viewModelScope.launch {
            val user = userRepo.getUserByPhone(currentUserId.toLong())!!
            user.favourites.add(productId)
            userRepo.addUser(user)
        }

    }

    fun removeFromFavourites(productId:Int){

        _userFavs.remove(productId)
        updateCurrentProductFavStatus()
        viewModelScope.launch {
            val user = userRepo.getUserByPhone(currentUserId.toLong())!!
            user.favourites.remove(productId)
            userRepo.addUser(user)
        }

    }

    fun addToCart(){

        viewModelScope.launch {
            val tempUser = userRepo.getUserByPhone(currentUserId.toLong())!!
            if(tempUser.cartItems.contains(product.value?.id)){
                tempUser.cartItems[product.value?.id!!.toInt()] = tempUser.cartItems[product.value?.id]!! + 1
            }else{
                tempUser.cartItems[product.value?.id!!] = 1
            }

            userRepo.updateUser( tempUser )

        }

    }

    suspend fun getRelevantProducts():List<ShopApiProductsResponse> {

        val relevantProducts = mutableListOf<ShopApiProductsResponse>()
        val allProducts = shopRepo.getProductFromCategory(product.value!!.category)
        repeat(6) {
            relevantProducts.add(allProducts.random())
        }

        Log.d("PROD_VIEWMODEL","returning: ${relevantproduct.toList()}")
        return relevantProducts
    }

    suspend fun getRandomProducts():List<ShopApiProductsResponse> {

        val randomProducts = mutableListOf<ShopApiProductsResponse>()
        val allProducts = shopRepo.getallProducts()
        while(randomProducts.size <= 5){
//            randomProducts.add(allProducts.random())
            allProducts.random().let {
                if(!randomProducts.contains(it)){
                    randomProducts.add(it)
                }
            }
        }
        return randomProducts
    }

//    fun setProduct(productDetails: ShopApiProductsResponse) {
//
//        viewModelScope.launch {
//
//            _relevantProducts.clear()
//            _otherProducts.clear()
//
//            _product.value = productDetails
//            _relevantProducts.addAll(getRandomProducts())
//            _otherProducts.addAll(getRelevantProducts())
//
//            Log.d(
//                "PROD_VIEWMODEL",
//                "Rel: ${relevantproduct.toList()}\n\nOther: ${otherPproducts.toList()}"
//            )
//        }
//    }


}