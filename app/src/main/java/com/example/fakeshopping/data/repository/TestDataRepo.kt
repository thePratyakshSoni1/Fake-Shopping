package com.example.fakeshopping.data.repository

import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.test_data.TestApiService
import kotlinx.coroutines.delay

class TestDataRepo: ShopApiRepository {
    override suspend fun getallProducts(): List<ShopApiProductsResponse> {
        delay(2000L)
        return TestApiService().getAllProducts()
    }

    override suspend fun getAllCategories(): List<String> {
        val tempList = mutableListOf<String>()
        tempList.addAll(TestApiService().getAllCategories())
        tempList.add(0,"All")
        delay(1500L)
        return tempList
    }

    override suspend fun getAllJewelery(): List<ShopApiProductsResponse> {
        delay(1500L)
        return TestApiService().getJweleryProducts()
    }

    override suspend fun getAllMensClothes(): List<ShopApiProductsResponse> {
        delay(1500L)
        return TestApiService().getMensProducts()
    }

    override suspend fun getAllElectronics(): List<ShopApiProductsResponse> {
        delay(2000L)
        return TestApiService().getElectronicsProducts()
    }

    override suspend fun getAllWomensClothes(): List<ShopApiProductsResponse> {
        delay(2000L)
        return TestApiService().getWomensProducts()
    }

    override suspend fun getProductFromCategory(category: String): List<ShopApiProductsResponse> {
        delay(2000L)
        return when(category){
            "electronics" ->{
                TestApiService().getJweleryProducts()
            }
            "All" ->{
                TestApiService().getAllProducts()
            }
            "jewelery" -> {
                TestApiService().getJweleryProducts()
            }
            "men's clothing" -> {
                TestApiService().getMensProducts()
            }
            "women's clothing" -> {
                TestApiService().getWomensProducts()
            }
            else -> TestApiService().getAllProducts()
        }

    }
}