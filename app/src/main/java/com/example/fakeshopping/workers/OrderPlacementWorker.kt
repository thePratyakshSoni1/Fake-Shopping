package com.example.fakeshopping.workers

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.fakeshopping.data.userdatabase.UserDatabase
import com.example.fakeshopping.utils.OrderDeliveryStatus
import kotlinx.coroutines.runBlocking

class OrderPlacementWorker(ctx: Context, workParams:WorkerParameters):Worker(ctx, workParams) {

    override fun doWork(): Result {

        val userDao = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "users_db"
        ).build()

        Log.d("WORKER_ORDER_EXTR","UserId: ${inputData.keyValueMap[OrderWorkerKeys.KEY_CURRENT_USER_ID.value].toString()} - OrderId: ${inputData.keyValueMap[OrderWorkerKeys.KEY_ORDEID.value].toString()}")



        runBlocking {
            val tempU = userDao.dao.getUserByPhone(inputData.keyValueMap[OrderWorkerKeys.KEY_CURRENT_USER_ID.value].toString().toLong())!!
            val  orderId = inputData.keyValueMap[OrderWorkerKeys.KEY_ORDEID.value].toString()
            val tempOrder = tempU.userOrders.find {
                Log.d("FINDING_ORDERID","${it.orderId} == $orderId")
                it.orderId.toString() == orderId
            }!!

            tempU.userOrders.remove(tempOrder)

            tempOrder.orderDeliveryStatus = OrderDeliveryStatus.STATUS_PLACED.value
            tempU.userOrders.add(tempOrder)
            userDao.dao.updateUser(tempU)

        }

       // [{"orderDateTime":"18-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5530434181122,"paymentMethod":3,"productId":[8],"productQuantity":[1],"razorpayPaymentId":"pay_KhZxdA0xPgd2h1","totalAmountPaid":106.97},{"orderDateTime":"18-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5537066181122,"paymentMethod":3,"productId":[6],"productQuantity":[1],"razorpayPaymentId":"pay_Kha46jGiCfgaHo","totalAmountPaid":106.97},{"orderDateTime":"18-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5534514181122,"paymentMethod":3,"productId":[8],"productQuantity":[1],"razorpayPaymentId":"pay_KhaEDdQnD8gxZH","totalAmountPaid":106.97},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5533976191122,"paymentMethod":3,"productId":[8],"productQuantity":[1],"razorpayPaymentId":"pay_KhlDRqmZnytgEb","totalAmountPaid":106.97},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5531648191122,"paymentMethod":3,"productId":[4],"productQuantity":[1],"razorpayPaymentId":"pay_KhlGYyb6YV7Ad0","totalAmountPaid":106.97},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5532216191122,"paymentMethod":3,"productId":[14],"productQuantity":[1],"razorpayPaymentId":"pay_KhlYGMWbxlT6vD","totalAmountPaid":106.97},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5537328191122,"paymentMethod":3,"productId":[8],"productQuantity":[1],"razorpayPaymentId":"pay_Khlcgbam1NWViS","totalAmountPaid":106.97},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5534119191122,"paymentMethod":3,"productId":[1,8],"productQuantity":[1,1],"razorpayPaymentId":"pay_KhlmeOOsTYLweZ","totalAmountPaid":213.94},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5536728191122,"paymentMethod":3,"productId":[10],"productQuantity":[1],"razorpayPaymentId":"pay_KhmDXf588nrcrp","totalAmountPaid":106.97},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5531626191122,"paymentMethod":3,"productId":[8,9,10],"productQuantity":[1,2,1],"razorpayPaymentId":"pay_KhmSV68OB9J7Zt","totalAmountPaid":422.88},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5533174191122,"paymentMethod":3,"productId":[8],"productQuantity":[1],"razorpayPaymentId":"pay_KhmUXOUAzkghmk","totalAmountPaid":106.97},{"orderDateTime":"19-11-2022","orderDeliveryAddress":"near Lux bar, 553399 LA, Washington DC, US","orderDeliveryStatus":0,"orderId":5532418191122,"paymentMethod":3,"productId":[8],"productQuantity":[1],"razorpayPaymentId":"pay_Khn6v2D0qNT2mm","totalAmountPaid":106.97}]

        makeNotfication("Order Placed", applicationContext)
        return Result.success()

    }

}