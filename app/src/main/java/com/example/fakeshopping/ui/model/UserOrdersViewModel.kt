package com.example.fakeshopping.ui.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class UserOrdersViewModel @Inject constructor( private val userRepo:UserRepository, private val shopRepo:ShopApiRepository ) : ViewModel() {

    private var _currentUserId:Long = 0L
    val currentUserId:Long get() = _currentUserId
    private var _userOrders = mutableStateListOf<UserOrders>()
    val userOrders get() = _userOrders as List<UserOrders>

    private val _userOrderProductDetail = mutableStateListOf<ShopApiProductsResponse>()
    val userOrderProductDetail get() = _userOrderProductDetail as List<ShopApiProductsResponse>

    fun updateUserOrdersUi(){
        viewModelScope.launch{
            _userOrders.clear()
            _userOrderProductDetail.clear()

            for(items in userRepo.getUserOrders(currentUserId)){
                for(productId in items.productId ){
                    _userOrderProductDetail.add( shopRepo.getProductbyId(productId) )
                }
                _userOrders.add( items )
            }
            
            _userOrders.sortByDescending {
                it.orderDateTime.toString().split("-")[0]
            }
        }
    }

    fun setCurrentUser(current:Long){
        _currentUserId = current
        updateUserOrdersUi()
    }

}