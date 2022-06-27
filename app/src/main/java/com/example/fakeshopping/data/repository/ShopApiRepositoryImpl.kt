package com.example.fakeshopping.data.repository

import com.example.fakeshopping.data.FakeShopApi
import com.example.fakeshopping.data.ShopApiProductsResponse

class ShopApiRepositoryImpl(private val shopApiService:FakeShopApi): ShopApiRepository {

    override suspend fun getallProducts(): List<ShopApiProductsResponse> {
        return shopApiService.getallProducts()
    }

    override suspend fun getAllCategories(): List<String> {
        return shopApiService.getAllCategories()
    }

    override suspend fun getAllJewelery(): ShopApiProductsResponse {
        return shopApiService.getAllJewelery()
    }

    override suspend fun getAllMensClothes(): ShopApiProductsResponse {
        return shopApiService.getAllMensClothes()
    }

    override suspend fun getAllElectronics(): ShopApiProductsResponse {
        return shopApiService.getAllElectronics()
    }

    override suspend fun getAllWomensClothes(): ShopApiProductsResponse {
        return shopApiService.getAllWomensClothes()
    }

}