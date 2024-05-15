package com.srinivasan.contacts.di.module

import com.srinivasan.contacts.data.remote.ContactsApi
import com.srinivasan.contacts.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        converterFactory: GsonConverterFactory
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .client(OkHttpClient())
            .build()

    }

    @Provides
    @Singleton
    fun provideContactsApi(
        retrofit: Retrofit
    ): ContactsApi = retrofit.create(ContactsApi::class.java)

}