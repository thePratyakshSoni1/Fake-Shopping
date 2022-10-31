package com.example.fakeshopping.data.userdatabase

data class UserOrders(

    val orderId:Long,
    val productId:List<Int>,
    val orderDateTime:String,
    val orderDelivered:Boolean?,
    val orderDeliverTime:String?,
    val productQuantity:List<Int>,
    val orderDeliveryAddress:String,
    val totalAmountPaid:Float,
    val paymentMethod:Int,
    val razorpayPaymentId:String

)
