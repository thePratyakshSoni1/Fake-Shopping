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
class MyProfileScreenViewmodel @Inject constructor(private val userRepository:UserRepository ) : ViewModel() {

    private val _currentUserAddress = mutableStateOf<UserAddress?>(null)
    val currentUserAddress get() = _currentUserAddress as State<UserAddress?>

    private val _currentUserFirstName = mutableStateOf("")
    val currentUserFirstName get() = _currentUserFirstName as State<String>

    private val _currentUserLastName = mutableStateOf("")
    val currentUserLastName get() = _currentUserLastName as State<String>

    private val _currentUserId = mutableStateOf("")
    val currentUserId get() = _currentUserId as State<String>

    private val _currentUserDetails = mutableStateOf<Users?>(null)
    val currentUserDetails get() = _currentUserDetails as State<Users?>

    private val _isPasswordVisible = mutableStateOf( false )
    val isPasswordVisible get() = _isPasswordVisible as State<Boolean>

    fun togglePasswordVisibility(){
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun verifyOldPassword(oldPassword:String):Boolean{
        return oldPassword == currentUserDetails.value!!.password
    }

    fun verifyNewPasswordPattern(newPassword:String):Boolean{

        val alphaPattern = Regex("[a-zA-Z]+")
        val numPattern = Regex("[0-9]+")
        val symbolPattern = Regex("""[&|“|`|´|}|{|°|>|<|:|.|;|#|'|)|(|@|_|$|"|!|?|*|=|^|-]+""")

        return (newPassword.contains(alphaPattern) && newPassword.contains(numPattern) && newPassword.contains(symbolPattern))
    }

    fun updatePassword(value:String){

        viewModelScope.launch {
            val tempUser = userRepository.getUserByPhone(currentUserId.value.toLong())!!
            tempUser.password = value
            userRepository.updateUser(tempUser)

            _currentUserDetails.value = userRepository.getUserByPhone(currentUserId.value.toLong())
            _currentUserFirstName.value = _currentUserDetails.value!!.userFirstName
            _currentUserLastName.value = _currentUserDetails.value!!.userLastName
            _currentUserAddress.value = _currentUserDetails.value!!.userAddress
        }

    }

    fun setUserIdAndDetails(userId:String){
        _currentUserId.value = userId
        viewModelScope.launch {
            _currentUserDetails.value = userRepository.getUserByPhone(currentUserId.value.toLong())
            _currentUserFirstName.value = _currentUserDetails.value!!.userFirstName
            _currentUserLastName.value = _currentUserDetails.value!!.userLastName
            _currentUserAddress.value = _currentUserDetails.value!!.userAddress
        }
    }

    fun deleteAccount(){
        viewModelScope.launch {
            userRepository.removeUser( currentUserId.value.toLong() )
        }
    }

}