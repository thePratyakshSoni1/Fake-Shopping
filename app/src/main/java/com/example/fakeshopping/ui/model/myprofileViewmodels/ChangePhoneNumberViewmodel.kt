package com.example.fakeshopping.ui.model.myprofileViewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePhoneNumberViewmodel @Inject constructor(private val userRepository: UserRepository) :ViewModel() {

    private val _currentUserId = mutableStateOf("")
    val currentUserId get() = _currentUserId as State<String>

    private val _isVerificationStep = mutableStateOf(false)
    val isVerificationStep get() = _isVerificationStep as State<Boolean>

    private val _isPassswordVisible = mutableStateOf(false)
    val isPassswordVisible get() = _isPassswordVisible as State<Boolean>

    private val _currentUserDetails = mutableStateOf<Users?>(null)
    val currentUserDetails get() = _currentUserDetails as State<Users?>

    private val _newPhoneNumber = mutableStateOf("")
    val newPhoneNumber get() = _newPhoneNumber as State<String>

    private val _password = mutableStateOf("")
    val password get() = _password as State<String>

    private val _code = mutableStateOf("")
    val code get() = _code as State<String>

    fun setUserIdAndDetails(userId:String){
        _currentUserId.value = userId
        viewModelScope.launch {
            _currentUserDetails.value = userRepository.getUserByPhone(currentUserId.value.toLong())
        }
    }

    fun onCodeValueChange(newValue:String){
        _code.value = newValue
    }

    fun onPasswordValueChange(newValue:String){
        _password.value = newValue
    }

    fun onPhoneValueChange(newValue:String){
        _newPhoneNumber.value = newValue
    }

    fun toggleVerificationState(){
        _isVerificationStep.value = !isVerificationStep.value
    }

    fun togglePasswordVisibility(){
        _isPassswordVisible.value = !isPassswordVisible.value
    }

    fun onPasswordVerify():Boolean{
        return password.value == currentUserDetails.value?.password
    }

    suspend fun isValidNumber():Boolean{

        userRepository.getAllUsers().forEach{
            if(it.userPhoneNumer == newPhoneNumber.value.toLong()){
                return false
            }
        }

        return true
    }

    fun onUserPhoneUpdate(){

        viewModelScope.launch {
            val tempUser = userRepository.getUserByPhone(currentUserId.value.toLong())!!
            tempUser.userPhoneNumer = newPhoneNumber.value.toLong()
            userRepository.removeUser(currentUserId.value.toLong())
            userRepository.addUser(tempUser)
        }

    }



}