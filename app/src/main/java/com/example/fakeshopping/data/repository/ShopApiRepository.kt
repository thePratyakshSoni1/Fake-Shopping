package com.example.fakeshopping.data.repository

import com.example.fakeshopping.data.ShopApiProductsResponse

interface ShopApiRepository {

    suspend fun getallProducts(): List<ShopApiProductsResponse>

    suspend fun getProductbyId(productId: Int): ShopApiProductsResponse

    suspend fun getAllCategories(): List<String>

    suspend fun getAllJewelery(): List<ShopApiProductsResponse>

    suspend fun getAllMensClothes(): List<ShopApiProductsResponse>

    suspend fun getAllElectronics(): List<ShopApiProductsResponse>

    suspend fun getAllWomensClothes(): List<ShopApiProductsResponse>

    suspend fun getProductFromCategory(category: String): List<ShopApiProductsResponse>


}