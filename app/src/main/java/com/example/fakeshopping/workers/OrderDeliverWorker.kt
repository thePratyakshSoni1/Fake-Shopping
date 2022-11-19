package com.example.fakeshopping.workers

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.*
import com.example.fakeshopping.data.userdatabase.UserDatabase
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.utils.OrderDeliveryStatus
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class OrderDeliverWorker(ctx: Context, workParams:WorkerParameters):Worker(ctx, workParams) {

    override fun doWork(): Result {

        val userDao = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "users_db"
        ).build()

        Log.d("WORKER_ORDER_EXTR","UserId: ${inputData.keyValueMap[OrderWorkerKeys.KEY_CURRENT_USER_ID.value].toString()} - OrderId: ${inputData.keyValueMap[OrderWorkerKeys.KEY_ORDEID.value].toString()}")

        lateinit var tempU: Users
        lateinit var orderId:String

        runBlocking {
            tempU = userDao.dao.getUserByPhone(inputData.keyValueMap[OrderWorkerKeys.KEY_CURRENT_USER_ID.value].toString().toLong())!!
            orderId = inputData.keyValueMap[OrderWorkerKeys.KEY_ORDEID.value].toString()
            val tempOrder = tempU.userOrders.find {
                Log.d("FINDING_ORDERID","${it.orderId} == $orderId")
                it.orderId.toString() == orderId
            }!!

            tempU.userOrders.remove(tempOrder)

            tempOrder.orderDeliveryStatus = OrderDeliveryStatus.STATUS_DELIVERED.value
            tempU.userOrders.add(tempOrder)
            userDao.dao.updateUser(tempU)

        }

        makeNotfication("Order Delivered", applicationContext)
        return Result.success()

    }


}