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

class OrderShippingWorker(ctx: Context, workParams: WorkerParameters): Worker( ctx, workParams) {


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

            tempOrder.orderDeliveryStatus = OrderDeliveryStatus.STATUS_SHIPPED.value
            tempU.userOrders.add(tempOrder)
            userDao.dao.updateUser(tempU)

        }

        makeNotfication("Order Shipped", applicationContext)

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.beginUniqueWork(
            "FSWORKER_ORDERSHIPPMENT_UNIQUEWORK",
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<OrderDeliverWorker>().apply{
                setInitialDelay((0..48).random().toLong(), TimeUnit.SECONDS)
                setInputData(
                    Data.Builder()
                        .putAll(
                            mutableMapOf(
                                OrderWorkerKeys.KEY_ORDEID.value to orderId,
                                OrderWorkerKeys.KEY_CURRENT_USER_ID.value to tempU.userPhoneNumer.toString()
                            ) as Map<String, Any>
                        )
                        .build()
                )
            }.build()
        ).enqueue()


        return Result.success()

    }


}