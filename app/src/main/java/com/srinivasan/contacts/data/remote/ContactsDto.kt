package com.srinivasan.contacts.data.remote

import com.google.gson.annotations.SerializedName

data class ContactsDto(
    @SerializedName("results")
    val contacts: List<ContactDetails>,
    @SerializedName("info")
    val info: ResponseInfo,
)

data class ContactDetails(
    val gender: String,
    val name: Name,
    val email: String,
    val phone: String,
    val cell: String,
    val id: Id,
    val picture: Picture,
)

data class Name(
    val title: String,
    val first: String,
    val last: String,
)

data class Id(
    val name: String,
    val value: String?,
)

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String,
)

data class ResponseInfo(
    val seed: String,
    val results: Int,
    val page: Int,
    val version: String,
)
