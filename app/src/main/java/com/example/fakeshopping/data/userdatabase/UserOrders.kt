package com.example.fakeshopping.data.userdatabase

data class UserOrders(

    val orderId:Long,
    val productId:Int,
    val orderDateTime:String,
    val orderDelivered:Boolean?,
    val orderDeliverTime:String?,
    val productQuantity:Int,
    val orderDeliveryAddress:String

)
