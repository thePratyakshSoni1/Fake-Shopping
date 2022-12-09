package com.example.fakeshopping.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fakeshopping.data.BASE_URL
import com.example.fakeshopping.data.FakeShopApi
import com.example.fakeshopping.data.repository.ShopApiRepository
import com.example.fakeshopping.data.repository.ShopApiRepositoryImpl
import com.example.fakeshopping.data.repository.TestDataRepo
import com.example.fakeshopping.data.userdatabase.UserDatabase
import com.example.fakeshopping.data.userdatabase.repository.UserRepository
import com.example.fakeshopping.data.userdatabase.repository.UserRepositoryImpl
import com.example.fakeshopping.data.usersettingsdatabse.UserSettingsDatabase
import com.example.fakeshopping.data.usersettingsdatabse.repository.UserSettingRepository
import com.example.fakeshopping.data.usersettingsdatabse.repository.UserSettingsRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FakeshopModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): FakeShopApi {
        return retrofit.create(FakeShopApi::class.java)
    }


    @Provides
    @Singleton
    fun provideshopApiRepository(apiService: FakeShopApi): ShopApiRepository {
        return ShopApiRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideTestDataApiService(): TestDataRepo {
        return TestDataRepo()
    }

    @Provides
    @Singleton
    fun provideUsersDatabase(app:Application): UserDatabase{
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            "users_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUsersRepository(db: UserDatabase): UserRepository {
        return UserRepositoryImpl(db.dao)
    }


    @Provides
    @Singleton
    fun provideUsersSettingsDatabase(app:Application): UserSettingsDatabase{
        return Room.databaseBuilder(
            app,
            UserSettingsDatabase::class.java,
            "users_settings_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUsersSettingsRepository(db: UserSettingsDatabase): UserSettingRepository {
        return UserSettingsRepositoryImpl(db.dao)
    }


}