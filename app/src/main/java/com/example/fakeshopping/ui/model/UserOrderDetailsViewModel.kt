package com.example.fakeshopping.ui.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
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
import kotlin.properties.Delegates

@HiltViewModel
class UserOrderDetailsViewModel @Inject constructor(private val shopRepo: ShopApiRepository, private val userRepo:UserRepository ) : ViewModel() {

    private lateinit var _currentUserId:String
    val currentUserId get() = _currentUserId

    private var _currentOrderId by Delegates.notNull<Long>()
    val currentOrderId get() = _currentOrderId

    private val _orderProductDetails = mutableStateMapOf<ShopApiProductsResponse, Int>()
    val orderProductDetails get() = _orderProductDetails as Map<ShopApiProductsResponse, Int>

    private var _order = mutableStateOf<UserOrders?>(null)
    val order get() = _order as State<UserOrders?>

    private var _totalItems = mutableStateOf(0)
    val totalItems get() = _totalItems as State<Int>


    fun setCurrentUserAndOrder(userId:String, orderId:Long){

        _currentOrderId = orderId
        _currentUserId = userId

        viewModelScope.launch {

            _order.value = userRepo.getUserOrders(currentUserId.toLong()).find {
                it.orderId == currentOrderId
            }!!

            for(item in order.value?.productId?.indices!!){
                val tempProduct = shopRepo.getProductbyId(order.value!!.productId[item])
                _orderProductDetails[tempProduct] = order.value!!.productQuantity[item]
            }

            order.value!!.productQuantity.forEach {
                _totalItems.value += it
            }

        }

    }


}