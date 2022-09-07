package com.example.fakeshopping.data.userdatabase

import android.util.Log
import androidx.room.TypeConverter
import com.example.fakeshopping.data.userdatabase.repository.UserAddress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserAddressToStringConverters {

    @TypeConverter
    fun intAddressToJson(address:UserAddress):String{
        return Gson().toJson(address)
    }

    @TypeConverter
    fun jsonToAddress(jsonString:String):UserAddress{
        val type = object: TypeToken<UserAddress>(){}.type
        val address:UserAddress = Gson().fromJson(jsonString, type )
        Log.i("GSON","To IntList: $address")
        return address
    }


}

class IntListTypeConverters {

    @TypeConverter
    fun intListToJson(list:MutableList<Int>):String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToIntList(jsonString:String):MutableList<Int>{
        val type = object: TypeToken<MutableList<Int>>(){}.type
        val list:MutableList<Int> = Gson().fromJson(jsonString, type )
        Log.i("GSON","To IntList: $list")
        return list
    }


}

class UserOrderListTypeConverters {

    @TypeConverter
    fun userOrderListToJson(list:MutableList<UserOrders>):String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToUserOrderList(jsonString:String):MutableList<UserOrders>{
        val type = object: TypeToken<MutableList<UserOrders>>(){}.type
        val list:MutableList<UserOrders> = Gson().fromJson(jsonString, type )
        Log.i("GSON","To IntList: $list")
        return list
    }


}

class MapTypeConverters{

    @TypeConverter
    fun intMapToJson(map:MutableMap<Int,Int>):String{
        return Gson().toJson(map)
    }

    @TypeConverter
    fun jsonToIntMap(jsonString:String):MutableMap<Int,Int>{
        val type = object: TypeToken<MutableMap<Int,Int>>(){}.type
        val map:MutableMap<Int,Int> = Gson().fromJson(jsonString, type )
        Log.i("GSON","To IntList: $map")
        return map
    }

}
