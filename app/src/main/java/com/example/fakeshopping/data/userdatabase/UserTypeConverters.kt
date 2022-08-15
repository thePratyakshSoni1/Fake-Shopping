package com.example.fakeshopping.data.userdatabase

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntListTypeConverters {

    @TypeConverter
    fun intListToJson(list:List<Int>):String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToIntList(jsonString:String):List<Int>{
        val type = object: TypeToken<List<Int>>(){}.type
        val list:List<Int> = Gson().fromJson(jsonString, type )
        Log.i("GSON","To IntList: $list")
        return list
    }


}

class UserOrderListTypeConverters {

    @TypeConverter
    fun userOrderListToJson(list:List<UserOrders>):String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToUserOrderList(jsonString:String):List<UserOrders>{
        val type = object: TypeToken<List<UserOrders>>(){}.type
        val list:List<UserOrders> = Gson().fromJson(jsonString, type )
        Log.i("GSON","To IntList: $list")
        return list
    }


}

class MapTypeConverters{

    @TypeConverter
    fun intMapToJson(map:Map<Int,Int>):String{
        return Gson().toJson(map)
    }

    @TypeConverter
    fun jsonToIntMap(jsonString:String):Map<Int,Int>{
        val type = object: TypeToken<Map<Int,Int>>(){}.type
        val map:Map<Int,Int> = Gson().fromJson(jsonString, type )
        Log.i("GSON","To IntList: $map")
        return map
    }

}
