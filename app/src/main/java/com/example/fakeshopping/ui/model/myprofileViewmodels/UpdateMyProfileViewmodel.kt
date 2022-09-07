package com.example.fakeshopping.ui.model.myprofileViewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.data.userdatabase.repository.UserAddress
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateMyProfileViewmodel @Inject constructor(private val  userRepository: UserRepository ) : ViewModel() {

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

    private val _currentUserFirstName = mutableStateOf("")
    val currentUserFirstName get() = _currentUserFirstName as State<String>

    private val _currentUserLastName = mutableStateOf("")
    val currentUserLastName get() = _currentUserLastName as State<String>

    private val _currentUserId = mutableStateOf("")
    val currentUserId get() = _currentUserId as State<String>

    private val _currentUserDetails = mutableStateOf<Users?>(null)
    val currentUserDetails get() = _currentUserDetails as State<Users?>

    fun setUserIdAndDetails(userId:String){
        _currentUserId.value = userId
        viewModelScope.launch {
            _currentUserDetails.value = userRepository.getUserByPhone(currentUserId.value.toLong())

            _currentUserFirstName.value = _currentUserDetails.value!!.userFirstName
            _currentUserLastName.value = _currentUserDetails.value!!.userLastName
            initilizeUserAddress(currentUserDetails.value?.userAddress)
        }
    }

    fun initilizeUserAddress(userAddress:UserAddress?){
        if(userAddress != null) {
            _currentUserCountry.value = userAddress.country
            _currentUserState.value = userAddress.state
            _currentUserCity.value = userAddress.city
            _currentUserPincode.value = userAddress.pincode.toString()
            _currentUserLandmark.value = userAddress.landmark
        }
    }

    fun updateUser(){

        val tempUser = currentUserDetails.value!!

        tempUser.userFirstName = currentUserFirstName.value
        tempUser.userLastName = currentUserLastName.value
        tempUser.userAddress.country = currentUserCountry.value
        tempUser.userAddress.state = currentUserState.value
        tempUser.userAddress.city = currentUserCity.value
        tempUser.userAddress.landmark = currentUserLandmark.value
        tempUser.userAddress.pincode = currentUserPincode.value.toInt()

        viewModelScope.launch {
            userRepository.updateUser(tempUser)
        }

    }

    fun verifyName():Boolean{
        return !(currentUserFirstName.value.isEmpty() || currentUserFirstName.value.contains(Regex("[^A-Za-z]")))
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

    fun onCityTextValueChange(newValue:String){
        _currentUserDetails.value?.userAddress?.city = newValue
        _currentUserCity.value = newValue
    }

    fun onCountryTextValueChange(newValue:String){
        _currentUserDetails.value?.userAddress?.country = newValue
        _currentUserCountry.value = newValue
    }

    fun onStateTextValueChange(newValue:String){
        _currentUserDetails.value?.userAddress?.state = newValue
        _currentUserState.value = newValue
    }

    fun onPincodeTextValueChange(newValue:Int){
        _currentUserDetails.value?.userAddress?.pincode = newValue
        _currentUserPincode.value = newValue.toString()
    }

    fun onLandMarkTextValueChange(newValue:String){
        _currentUserDetails.value?.userAddress?.landmark = newValue
        _currentUserLandmark.value = newValue
    }

    fun onFirstNameValueChange(newValue:String){
        _currentUserDetails.value?.userFirstName = newValue
        _currentUserFirstName.value = newValue
    }

    fun onLastNameValueChange(newValue:String){
        _currentUserDetails.value?.userLastName = newValue
        _currentUserLastName.value = newValue
    }



}