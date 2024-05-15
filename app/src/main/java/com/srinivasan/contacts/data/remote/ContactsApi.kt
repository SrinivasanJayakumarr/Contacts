package com.srinivasan.contacts.data.remote

import com.srinivasan.contacts.util.Constants.INC
import com.srinivasan.contacts.util.Constants.PAGE
import com.srinivasan.contacts.util.Constants.RESULTS
import retrofit2.http.GET
import retrofit2.http.Query

interface ContactsApi {

    @GET
    suspend fun getContacts(
        @Query(value = RESULTS) results: Int = 25,
        @Query(value = INC) inc: String = "gender,name,picture,phone,cell,id,email",
    ): ContactsDto

    @GET(".")
    suspend fun getContactsPaging(
        @Query(value = PAGE) page: Int,
        @Query(value = RESULTS) results: Int = 10,
        @Query(value = INC) inc: String = "gender,name,picture,phone,cell,id,email",
    ): ContactsDto

}