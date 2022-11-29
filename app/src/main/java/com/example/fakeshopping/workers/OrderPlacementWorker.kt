package com.example.fakeshopping.workers

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.*
import com.example.fakeshopping.data.BASE_URL
import com.example.fakeshopping.data.FakeShopApi
import com.example.fakeshopping.data.repository.ShopApiRepositoryImpl
import com.example.fakeshopping.data.userdatabase.UserDatabase
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.deliverydepartment_notifications.DeliveryNotificationService
import com.example.fakeshopping.utils.OrderDeliveryStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class OrderPlacementWorker(ctx: Context, workParams:WorkerParameters):Worker(ctx, workParams) {

    override fun doWork(): Result {

        val userDao = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "users_db"
        ).build()

        Log.d("WORKER_ORDER_EXTR","UserId: ${inputData.keyValueMap[OrderWorkerKeys.KEY_CURRENT_USER_ID.value].toString()} - OrderId: ${inputData.keyValueMap[OrderWorkerKeys.KEY_ORDEID.value].toString()}")

        lateinit var tempU: Users
        lateinit var orderId:String
        var orderItems:String =""

        val fsApi = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build())).build()
            .create(FakeShopApi::class.java)

        val shopRepo = ShopApiRepositoryImpl( fsApi )

        runBlocking {
            tempU = userDao.dao.getUserByPhone(inputData.keyValueMap[OrderWorkerKeys.KEY_CURRENT_USER_ID.value].toString().toLong())!!
            orderId = inputData.keyValueMap[OrderWorkerKeys.KEY_ORDEID.value].toString()
            val tempOrder = tempU.userOrders.find {
                Log.d("FINDING_ORDERID","${it.orderId} == $orderId")
                it.orderId.toString() == orderId
            }!!

            for(items in tempOrder.productId){

                orderItems += ", "
                orderItems += shopRepo.getProductbyId(items).title

            }

            tempU.userOrders.remove(tempOrder)

            tempOrder.orderDeliveryStatus = OrderDeliveryStatus.STATUS_PLACED.value
            tempU.userOrders.add(tempOrder)
            userDao.dao.updateUser(tempU)

        }

        DeliveryNotificationService(applicationContext)
            .sendNotification("Order Shipped", "Your order for $orderItems has been placed", orderId.toLong())



        Log.d("DELIVERY_NOTIFY","Sent Notification for Placement")

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.beginUniqueWork(
            "FSWORKER_ORDERSHIPPMENT_UNIQUEWORK",
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<OrderShippingWorker>().apply{
                setInitialDelay((0..24).random().toLong(), TimeUnit.SECONDS)
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