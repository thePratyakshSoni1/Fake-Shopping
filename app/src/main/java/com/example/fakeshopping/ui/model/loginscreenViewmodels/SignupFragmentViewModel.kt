package com.example.fakeshopping.ui.model.loginscreenViewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.data.userdatabase.repository.UserAddress
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.data.usersettingsdatabse.Settings
import com.example.fakeshopping.data.usersettingsdatabse.repository.UserSettingRepository
import com.example.fakeshopping.utils.LoginSignupStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignupFragmentViewModel @Inject constructor(private val usersRepo: UserRepository, private val settingsRepo: UserSettingRepository ): ViewModel() {

    init{
        Log.d("SIGNUP","Now on signup screen viewModel initializing")
    }


    private val _code = mutableStateOf("")
    private val _isPasswordVisible = mutableStateOf( false )
    private val _password = mutableStateOf("")
    private val _lastName = mutableStateOf("")
    private val _confirmPassword = mutableStateOf("")
    private val _firstName = mutableStateOf("")
    private val _phone = mutableStateOf("")
    private val _dob = mutableStateOf("")
    private val _isOtpStep = mutableStateOf(false)

    val isPasswordVisible get() = _isPasswordVisible as State<Boolean>
    val isOtpStep get() = _isOtpStep as State<Boolean>
    val firstName =_firstName as State<String>
    val code = _code as State<String>
    val lastName = _lastName as State<String>
    val phone = _phone as State<String>
    val dob = _dob as State<String>
    val password = _password as  State<String>
    val confirmPassword = _confirmPassword as  State<String>

    fun togglePasswordVisibility(){
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

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

        val alphaPattern = Regex("[a-zA-Z]+")
        val numPattern = Regex("[0-9]+")
        val symbolPattern = Regex("""[&|“|`|´|}|{|°|>|<|:|.|;|#|'|)|(|@|_|$|"|!|?|*|=|^|-]+""")

        return if(_firstName.value.isEmpty() || _firstName.value.contains(Regex("[^A-Za-z]"))){
            LoginSignupStatus.STATUS_SIGNUP_INVALID_NAME
        }else if(_lastName.value.contains(Regex("[^A-Za-z]"))){
            LoginSignupStatus.STATUS_SIGNUP_INVALID_NAME
        }else if(!_phone.value.contains(Regex("[0-10]")) && _phone.value.length == 10 ){
            LoginSignupStatus.STATUS_SIGNUP_INVALID_NUMBER
        }else if(!(_password.value.contains(alphaPattern) && _password.value.contains(numPattern) && _password.value.contains(symbolPattern))){
            LoginSignupStatus.STATUS_SIGNUP_INVALID_PASSWORD_PATTERN
        }else if(_password.value != _confirmPassword.value){
            LoginSignupStatus.STATUS_SIGNUP_PASSWORD_UNMATCHED
        }else{
            LoginSignupStatus.STATUS_SIGNUP_SUCCESS
        }

    }


    fun addUser():LoginSignupStatus{

        if(verifySignUpDetails() == LoginSignupStatus.STATUS_SIGNUP_SUCCESS){

            viewModelScope.launch {

                settingsRepo.addUserSettings(
                    Settings(
                        userId = phone.value.toLong(),
                        isAddressChangeSchemeEnabled = true,
                        isOrderUpdatesnotificationsEnabled = true,
                        isOtpCodesNotifictionEnabled = true,
                        isEmailLettersEnabled = true
                    )
                )

            }

            runBlocking {
                usersRepo.addUser(
                    Users(
                        userPhoneNumer = phone.value.toLong(),
                        password = password.value,
                        favourites = mutableListOf<Int>(),
                        cartItems = mutableMapOf<Int,Int>(),
                        userAddress = UserAddress(
                            country = "",
                            city = "",
                            state = "",
                            pincode = 0,
                            landmark = ""
                        ),
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
