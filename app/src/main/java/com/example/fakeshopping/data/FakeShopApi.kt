package com.example.fakeshopping.data

import retrofit2.http.GET

const val BASE_URL = "https://fakestoreapi.com"

interface FakeShopApi {

    @GET("products")
    suspend fun getallProducts():List<ShopApiProductsResponse>

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>

    @GET("products/categories/jewelery")
    suspend fun getAllJewelery(): ShopApiProductsResponse

    @GET("products/categories/men's clothing")
    suspend fun getAllMensClothes(): ShopApiProductsResponse

    @GET("products/categories/electronics")
    suspend fun getAllElectronics(): ShopApiProductsResponse

    @GET("products/categories/women's clothing")
    suspend fun getAllWomensClothes(): ShopApiProductsResponse


}