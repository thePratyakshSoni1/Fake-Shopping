package com.example.fakeshopping.ui.model.loginscreenViewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.utils.LoginSignupStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewmodel @Inject constructor(val usersRepo: UserRepository): ViewModel() {

    private val _isPasswordVisible = mutableStateOf( false )
    val isPasswordVisible get() = _isPasswordVisible as State<Boolean>

    private val _phoneNumberTxt: MutableState<String> = mutableStateOf("")
    private val _passwordTxt: MutableState<String> = mutableStateOf("")

    val phoneNumberTxt:State<String> = _phoneNumberTxt as State<String>
    val passwordTxt:State<String> = _passwordTxt as State<String>

    fun togglePasswordVisibility(){
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun changePhoneText(newText:String){
        _phoneNumberTxt.value = newText
    }

    fun changePasswordText(newText:String){
        _passwordTxt.value = newText
    }

    fun verifyLogin():LoginSignupStatus {

        return if (_phoneNumberTxt.value.isEmpty() || _passwordTxt.value.isEmpty()){
            LoginSignupStatus.STATUS_INVALID_DETAILS
        }else {
            var user: Users?
            runBlocking {
                user = usersRepo.getUserByPhone(phoneNumberTxt.value.toLong())
            }

            if (user == null) {
                LoginSignupStatus.STATUS_LOGIN_NO_USER
            } else if (user!!.password != passwordTxt.value) {
                LoginSignupStatus.STATUS_LOGIN_WRONG_PASSWORD
            } else {
                LoginSignupStatus.STATUS_LOGIN_SUCCESS
            }
        }

    }

    fun getUser():String{
        var user:String = ""
        runBlocking {
            user = usersRepo.getUserByPhone(phoneNumberTxt.value.toLong())?.userFirstName!!

        }
        return user
    }


}