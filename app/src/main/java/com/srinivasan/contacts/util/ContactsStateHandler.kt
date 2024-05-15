package com.srinivasan.contacts.util

sealed class ContactsStateHandler<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T): ContactsStateHandler<T>(data)
    class Error<T>(message: String?, data: T? = null): ContactsStateHandler<T>(data,message)
    class Loading<T>: ContactsStateHandler<T>()

}