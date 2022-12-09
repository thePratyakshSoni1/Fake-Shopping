package com.example.fakeshopping.ui.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.usersettingsdatabse.repository.UserSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class SettingScreenViewModel @Inject constructor(private val settingsRepo:UserSettingRepository  ): ViewModel() {

    private val _currentUserId = mutableStateOf<String>("")
    val currentUserId get() = _currentUserId as State<String>

    private val _isMailLettersEnabled = mutableStateOf<Boolean>(false)
    val isMailLettersEnabled get() = _isMailLettersEnabled as State<Boolean>

    private val _isOrderupdatesEnabled = mutableStateOf<Boolean>(false)
    val isOrderupdatesEnabled get() = _isOrderupdatesEnabled as State<Boolean>

    private val _isOtpCodesEnabled = mutableStateOf<Boolean>(false)
    val isOtpCodesEnabled get() = _isOtpCodesEnabled as State<Boolean>

    private val _addressChangingSchemeEnabled = mutableStateOf<Boolean>(false)
    val addressChangingSchemeEnabled get() = _addressChangingSchemeEnabled as State<Boolean>


    fun setCurrentUserAndInitViewModel(currentUser:String){

        viewModelScope.launch {
            _currentUserId.value = currentUser
        }

        viewModelScope.launch {
            syncSettingUi(currentUser.toLong())
        }

    }

    private suspend fun syncSettingUi(currentUser: Long){

        settingsRepo.apply {

            _isMailLettersEnabled.value= isMailLettersEnabledForUser(currentUser)
            _isOtpCodesEnabled.value= isOtpCodesNotificationEnabled(currentUser)
            _isOrderupdatesEnabled.value= isOrderUpdatesNotificationEnabled(currentUser)
            _addressChangingSchemeEnabled.value= isAdressChangingSchemeEnabled(currentUser)

        }

    }

    fun onMailLettersSettingSwitchToggle(setToEnabled:Boolean){
        _isMailLettersEnabled.value = setToEnabled
        runBlocking {
            settingsRepo.updateUserSetting(
                settingsRepo.getUserSettings(currentUserId.value).apply {
                    isEmailLettersEnabled = isMailLettersEnabled.value
                }
            )
        }
    }

    fun onOrderUpdatesSettingSwitchToggle(setToEnabled:Boolean){
        _isOrderupdatesEnabled.value = setToEnabled
        runBlocking {
            settingsRepo.updateUserSetting(
                settingsRepo.getUserSettings(currentUserId.value).apply {
                    isOrderUpdatesnotificationsEnabled = _isOrderupdatesEnabled.value
                }
            )
        }
    }

    fun onOtpCodesSettingSwitchToggle(setToEnabled:Boolean){
        _isOtpCodesEnabled.value = setToEnabled
        runBlocking {
            settingsRepo.updateUserSetting(
                settingsRepo.getUserSettings(currentUserId.value).apply {
                    isOtpCodesNotifictionEnabled = isOtpCodesEnabled.value
                }
            )
        }
    }

    fun onAddressSchemeSettingSwitchToggle(setToEnabled:Boolean){
        _addressChangingSchemeEnabled.value = setToEnabled
        runBlocking {
            settingsRepo.updateUserSetting(
                settingsRepo.getUserSettings(currentUserId.value).apply {
                    isAddressChangeSchemeEnabled = _addressChangingSchemeEnabled.value
                }
            )
        }
    }

}