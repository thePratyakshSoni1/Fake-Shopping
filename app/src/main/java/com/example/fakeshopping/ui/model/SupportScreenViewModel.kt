package com.example.fakeshopping.ui.model

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SupportScreenViewModel @Inject constructor(  ): ViewModel() {

    private val _mailTitle = mutableStateOf("")
    val mailTitle get() = _mailTitle as State<String>

    private val _mailContent = mutableStateOf("")
    val mailContent get() = _mailContent as State<String>

    fun onMailTitleChange(newTxt:String){
        _mailTitle.value = newTxt
    }

    fun generateFeedbackIntent():Intent{
        val mailIntent = Intent(Intent.ACTION_SENDTO)
        mailIntent.apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, mailTitle.value)
            putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("pratyakshsoni2004@gmail.com"))
            putExtra(Intent.EXTRA_TEXT, mailContent.value)
        }
        return mailIntent
    }

    fun onMailContentChange(newTxt:String){
        _mailContent.value = newTxt
    }

}