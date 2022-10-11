package com.example.fakeshopping.ui.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.repository.UserAddress
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.utils.PaymentOptionId
import com.example.fakeshopping.utils.extractIntListStringToIntList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(private val shopRepo: TestDataRepo, private val userRepo: UserRepository ) :ViewModel(){

    private val _itemsToBuy = mutableStateMapOf<Int, Int>()
    val itemsToBuy get() = _itemsToBuy as Map<Int, Int>

    private val _cartItems = mutableStateMapOf<Int, Int>()
    val cartItems get() = _cartItems as Map<Int, Int>

    private val _isStoredAddressSelectedFordelivery = mutableStateOf(true)
    val isStoredAddressSelectedFordelivery get() = _isStoredAddressSelectedFordelivery as State<Boolean>

    private var _currentUserId:String = ""
    private val currentUserId get() = _currentUserId

    private val _discount = mutableStateOf(0f)
    val discount get() = _discount as State<Float>

    private val _totalCost = mutableStateOf(0f)
    val totalCost get() = _totalCost as State<Float>

    private val _itemsCost = mutableStateOf(0f)
    val itemsCost get() = _itemsCost as State<Float>

    private val _deliveryCharge = mutableStateOf(0f)
    val deliveryCharge get() = _deliveryCharge as State<Float>

    private val _tax = mutableStateOf(0f)
    val tax get() = _tax as State<Float>

    private val _paymentMethod = mutableStateOf(PaymentOptionId.OPTION_CARD)
    val paymentMethod get() = _paymentMethod as State<PaymentOptionId>

    private val _currentUserStoredAddress = mutableStateOf<UserAddress?>(null)
    val currentUserStoredAddress get() = _currentUserStoredAddress as State<UserAddress?>

    private val _currentUserCountry = mutableStateOf("")
    val currentUserCountry get() = _currentUserCountry as State<String>

    private val _currentUserState = mutableStateOf("")
    val currentUserState get() = _currentUserState as State<String>

    private val _currentUserCity = mutableStateOf("")
    val currentUserCity get() = _currentUserCity as State<String>

    private val _currentUserPincode = mutableStateOf("")
    val currentUserPincode get() = _currentUserPincode as State<String>

    private val _currentUserLandmark = mutableStateOf("")
    val currentUserLandmark get() = _currentUserLandmark as State<String>

    fun toggleDeleveryAddressMode(setToOldStoredAddress:Boolean){
        _isStoredAddressSelectedFordelivery.value = setToOldStoredAddress
    }

    fun setCurrentUser(userId:String, selectedProductsId:String, selectedProductsQuantity:String){

        _currentUserId = userId

        viewModelScope.launch{
            _currentUserStoredAddress.value = userRepo.getUserAddress(currentUserId.toLong())
            if(currentUserStoredAddress.value == null || currentUserStoredAddress.value!!.country.isEmpty()){
                _isStoredAddressSelectedFordelivery.value = false
            }
        }

        viewModelScope.launch{
            _itemsToBuy.clear()

            val tempItemsToBuyList = extractIntListStringToIntList(selectedProductsId)
            val tempItemsToBuyQuantityList = extractIntListStringToIntList(selectedProductsQuantity)

            Log.d("ITEMS_TO_BUY_LIST", tempItemsToBuyList.toString())
            Log.d("ITEMS_QUANTITY_LIST", tempItemsToBuyQuantityList.toString())

            for( index in tempItemsToBuyList.indices){
                _itemsToBuy[tempItemsToBuyList[index]] = tempItemsToBuyQuantityList[index]
            }
            reCalculateTotalCost()
        }

    }

    fun updateUserAddress(){
        viewModelScope.launch {
            val user = userRepo.getUserByPhone(currentUserId.toLong())!!
            user.userAddress.city = currentUserCity.value
            user.userAddress.pincode = currentUserPincode.value.toInt()
            user.userAddress.state = currentUserState.value
            user.userAddress.country = currentUserCountry.value
            user.userAddress.landmark= currentUserLandmark.value

            userRepo.updateUser(user)
        }

    }

    fun changeCurrentPaymentMethod(newMethodId:PaymentOptionId){
        _paymentMethod.value = newMethodId
    }


    fun verifyAddress():Boolean{
        val userAddress: UserAddress? = UserAddress(
            currentUserCountry.value,
            currentUserState.value,
            currentUserCity.value,
            currentUserPincode.value.toInt(),
            currentUserLandmark.value,
        )
        val regexAlphaPatern= Regex("[A-Za-z]")

        return if(userAddress != null){
            userAddress.city.isNotEmpty() && userAddress.city.contains(regexAlphaPatern) &&
                    userAddress.country.isNotEmpty() && userAddress.country.contains(regexAlphaPatern) &&
                    userAddress.state.isNotEmpty() && userAddress.state.contains(regexAlphaPatern) &&
                    userAddress.landmark.isNotEmpty() &&
                    userAddress.pincode.toString().length >= 6 && userAddress.pincode.toString().contains(Regex("[0-9]"))
        }else{
            false
        }

    }

    private suspend fun reCalculateTotalCost(){

        _itemsCost.value = 0f
        _totalCost.value = 0f
        _deliveryCharge.value = 0f
        _tax.value = 0f

        viewModelScope.launch {
            for( items in itemsToBuy){

                val tempItemCost = shopRepo.getProductbyId(items.key).price.toFloat()
                _itemsCost.value +=  tempItemCost * items.value
                _tax.value += (tempItemCost * items.value) * 0.03f
                _deliveryCharge.value += if( tempItemCost > 30f) 5f else 8f

            }

            _totalCost.value = (itemsCost.value + tax.value + deliveryCharge.value) - discount.value
        }

    }


    fun onCityTextValueChange(newValue:String){
        _currentUserCity.value = newValue
    }

    fun onCountryTextValueChange(newValue:String){
        _currentUserCountry.value = newValue
    }

    fun onStateTextValueChange(newValue:String){
        _currentUserState.value = newValue
    }

    fun onPincodeTextValueChange(newValue:Int){
        _currentUserPincode.value = newValue.toString()
    }

    fun onLandMarkTextValueChange(newValue:String){
        _currentUserLandmark.value = newValue
    }






}