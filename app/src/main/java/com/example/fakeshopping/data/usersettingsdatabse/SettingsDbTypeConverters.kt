package com.example.fakeshopping.data.usersettingsdatabse

import android.util.Log
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SettingValueToString{

    @TypeConverter
    fun booleanToString(boolValue:Boolean ):String{
        return Gson().toJson(boolValue)
    }

    @TypeConverters
    fun stringToBoolean(value:String):Boolean{
        val type = object: TypeToken<Boolean>(){}.type
        val settingValue:Boolean = Gson().fromJson(value,type)
        Log.d("GSON", "To Setting Boolean: $settingValue")
        return settingValue
    }


}