package com.example.fakeshopping.data

import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://fakestoreapi.com"

interface FakeShopApi {

    @GET("products")
    suspend fun getallProducts(): List<ShopApiProductsResponse>

    @GET("products/{id}")
    suspend fun getProductbyid(@Path("id") productId:Int): ShopApiProductsResponse

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>

    @GET("products/category/jewelery")
    suspend fun getAllJewelery(): List<ShopApiProductsResponse>

    @GET("products/category/men's clothing")
    suspend fun getAllMensClothes(): List<ShopApiProductsResponse>

    @GET("products/category/electronics")
    suspend fun getAllElectronics(): List<ShopApiProductsResponse>

    @GET("products/category/women's clothing")
    suspend fun getAllWomensClothes(): List<ShopApiProductsResponse>


}