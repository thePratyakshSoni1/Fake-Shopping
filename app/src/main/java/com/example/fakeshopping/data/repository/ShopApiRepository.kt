package com.example.fakeshopping.data.repository

import com.example.fakeshopping.data.ShopApiProductsResponse

interface ShopApiRepository {

    suspend fun getallProducts():List<ShopApiProductsResponse>

    suspend fun getAllCategories(): List<String>

    suspend fun getAllJewelery(): ShopApiProductsResponse

    suspend fun getAllMensClothes(): ShopApiProductsResponse

    suspend fun getAllElectronics(): ShopApiProductsResponse

    suspend fun getAllWomensClothes(): ShopApiProductsResponse


}