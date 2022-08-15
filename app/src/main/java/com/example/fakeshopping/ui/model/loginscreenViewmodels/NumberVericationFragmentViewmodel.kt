package com.example.fakeshopping.ui.model.loginscreenViewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NumberVericationFragmentViewmodel @Inject constructor(val usersRepo : UserRepository) : ViewModel() {

    private var _phone:String = ""
    val phone get() = _phone

    private val _code = mutableStateOf("")
    val code = _code as State<String>

    fun onCodechange(newtxt:String){

        if(_code.value.length <= 4){
            _code.value = newtxt
        }

    }

    fun verifyCode(){

    }


}