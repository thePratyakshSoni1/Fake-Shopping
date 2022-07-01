package com.example.fakeshopping.data.repository

import com.example.fakeshopping.data.FakeShopApi
import com.example.fakeshopping.data.ShopApiProductsResponse

class ShopApiRepositoryImpl(private val shopApiService: FakeShopApi) : ShopApiRepository {

    override suspend fun getallProducts(): List<ShopApiProductsResponse> {
        return shopApiService.getallProducts()
    }

    override suspend fun getAllCategories(): List<String> {
        val tempList = mutableListOf<String>()
        tempList.addAll(shopApiService.getAllCategories())
        tempList.add(0, "All")
        return tempList
    }

    override suspend fun getAllJewelery(): List<ShopApiProductsResponse> {
        return shopApiService.getAllJewelery()
    }

    override suspend fun getAllMensClothes(): List<ShopApiProductsResponse> {
        return shopApiService.getAllMensClothes()
    }

    override suspend fun getAllElectronics(): List<ShopApiProductsResponse> {
        return shopApiService.getAllElectronics()
    }

    override suspend fun getAllWomensClothes(): List<ShopApiProductsResponse> {
        return shopApiService.getAllWomensClothes()
    }

    override suspend fun getProductFromCategory(category: String): List<ShopApiProductsResponse> {

        return when (category) {
            "electronics" -> {
                getAllElectronics()
            }
            "All" -> {
                getallProducts()
            }
            "jewelery" -> {
                getAllJewelery()
            }
            "men's clothing" -> {
                getAllMensClothes()
            }
            "women's clothing" -> {
                getAllWomensClothes()
            }
            else -> getallProducts()
        }

    }

}