package com.example.fakeshopping.ui.model.loginscreenViewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.utils.LoginSignupStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignupFragmentViewModel @Inject constructor(val usersRepo: UserRepository): ViewModel() {

    init{
        Log.d("SIGNUP","Now on signup screen viewModel initializing")
    }

    private val _code = mutableStateOf("")
    private val _password = mutableStateOf("")
    private val _lastName = mutableStateOf("")
    private val _confirmPassword = mutableStateOf("")
    private val _firstName = mutableStateOf("")
    private val _phone = mutableStateOf("")
    private val _dob = mutableStateOf("")
    private val _isOtpStep = mutableStateOf(false)

    val isOtpStep get() = _isOtpStep as State<Boolean>
    val firstName =_firstName as State<String>
    val code = _code as State<String>
    val lastName = _lastName as State<String>
    val phone = _phone as State<String>
    val dob = _dob as State<String>
    val password = _password as  State<String>
    val confirmPassword = _confirmPassword as  State<String>

    fun toggleSignupStep(isVericationStep:Boolean){
        _isOtpStep.value = isVericationStep
    }

    fun clearCode(){
        _code.value = ""
    }

    fun onCodechange(newtxt:String){

        if(_code.value.length <= 4){
            _code.value = newtxt
        }

    }

    fun onFirstNameTxtChange (newTxt:String){
        _firstName.value = newTxt
    }

    fun onLastNameTxtChange (newTxt:String){
        _lastName.value = newTxt
    }

    fun onPhoneNumberTxtChange (newTxt:String){
        _phone.value = newTxt
    }

     fun onDobTxtChange (newTxt:String){
        _dob.value = newTxt
    }

    fun onPassWordTxtChange (newTxt:String){
        _password.value = newTxt
    }

    fun onConfirmPasswordtxtChange (newTxt:String){
        _confirmPassword.value = newTxt
    }

    fun verifySignUpDetails():LoginSignupStatus{

        return if(_firstName.value.isEmpty()){
            LoginSignupStatus.STATUS_SIGNUP_INVALID_NAME
//        }else if(!_lastName.value.contains(Regex("[^A-Za-z]"))){
//            LoginSignupStatus.STATUS_SIGNUP_INVALID_NAME
        }else if(!_phone.value.contains(Regex("[0-10]")) && _phone.value.length == 9 ){
            LoginSignupStatus.STATUS_SIGNUP_INVALID_NUMBER
        }else if(_password.value != _confirmPassword.value){
            LoginSignupStatus.STATUS_SIGNUP_PASSWORD_UNMATCHED
        }else{
            LoginSignupStatus.STATUS_SIGNUP_SUCCESS
        }

    }


    fun addUser():LoginSignupStatus{

        if(verifySignUpDetails() == LoginSignupStatus.STATUS_SIGNUP_SUCCESS){

            runBlocking {
                usersRepo.addUser(
                    Users(
                        userPhoneNumer = phone.value.toLong(),
                        password = password.value,
                        favourites = mutableListOf<Int>(),
                        cartItems = mutableMapOf<Int,Int>(),
                        userAddress = null,
                        userFirstName = firstName.value,
                        userLastName = lastName.value,
                        userOrders = mutableListOf<UserOrders>()
                    )
                )

                Log.d("USER_DATA", usersRepo.getUserByPhone(phone.value.toLong()).toString())
            }
            return LoginSignupStatus.STATUS_LOGIN_SUCCESS
        }else{
            return LoginSignupStatus.STATUS_SIGNUP_FAILED
        }

    }
}
