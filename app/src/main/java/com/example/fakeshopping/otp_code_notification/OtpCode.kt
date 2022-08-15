package com.example.fakeshopping.otp_code_notification

object OtpCode {

    private var _code:String? = ""
    val code get() = _code

    fun generateNewCode():String{

        val currentMills = System.currentTimeMillis().toString()
        val newOtpCode = currentMills.subSequence(currentMills.length-5,currentMills.length-1)
        _code = newOtpCode.toString()

        return code!!
    }


}