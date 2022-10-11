package com.example.fakeshopping.utils

import android.util.Log

fun extractIntListStringToIntList(listString:String):List<Int>{

    var tempStringList = listString.substring(1..(listString.length -2))
    val tempList = mutableListOf<Int>()

    /** By converting a list to string it contains next element after every ", " . **/

    tempStringList.split(", ").forEach {
        if(it.contains(Regex("[0-9]"))){
            tempList.add(it.toString().toInt())
            Log.d("UTIL_FUNC","Addig $it")
        }
    }

    return tempList

}
