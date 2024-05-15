package com.srinivasan.contacts.data.repository

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.provider.ContactsContract
import androidx.paging.Pager
import androidx.paging.PagingData
import com.srinivasan.contacts.data.room.dao.ContactsDao
import com.srinivasan.contacts.data.room.entity.ContactEntity
import com.srinivasan.contacts.di.module.IoDispatcher
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.util.mappers.toContact
import com.srinivasan.contacts.util.mappers.toContactEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

///////////////////////////////////////////////////////////////////////////////////////////////

// Created by Srinivasan Jayakumar on 10/May/2024 09:50

///////////////////////////////////////////////////////////////////////////////////////////////

class ContactsRepository @Inject constructor (
    private val contentResolver: ContentResolver,
    private val contactsPager: Pager<Int, ContactEntity>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val contactsDao: ContactsDao
) {

    suspend fun createNewContact(contact: Contact) {
        withContext(ioDispatcher) {

            println("Inside createNewContact --> $contact")

            // Create a new contact
            val newContactContentValues = ContentValues().apply {
                put(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, contact.name)
            }

            // Insert the contact into the Contacts Provider
            val contactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, newContactContentValues)

            // Insert the mobile number
            val nameContentValues = ContentValues().apply {
                put(ContactsContract.Data.RAW_CONTACT_ID, contactUri?.lastPathSegment?.toLong())
                put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.name)
            }
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, nameContentValues)

            // Insert the mobile number
            contact.mobile?.let {
                val mobileContentValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, contactUri?.lastPathSegment?.toLong())
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Phone.NUMBER, it)
                    put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                }
                contentResolver.insert(ContactsContract.Data.CONTENT_URI, mobileContentValues)
            }

            // Insert the phone number
            contact.phone?.let {
                val phoneContentValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, contactUri?.lastPathSegment?.toLong())
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Phone.NUMBER, it)
                    put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
                }
                contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneContentValues)
            }

            // Insert the email address
            contact.email?.let {
                val emailContentValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, contactUri?.lastPathSegment?.toLong())
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Email.ADDRESS, it)
                    put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                }
                contentResolver.insert(ContactsContract.Data.CONTENT_URI, emailContentValues)
            }

            // Insert the photo URI
            contact.photo?.let {
                val photoContentValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, contactUri?.lastPathSegment?.toLong())
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Photo.PHOTO_URI, it)
                }
                contentResolver.insert(ContactsContract.Data.CONTENT_URI, photoContentValues)
            }

            // Insert the photo thumbnail URI
            contact.photoThumbnail?.let {
                val photoThumbnailContentValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, contactUri?.lastPathSegment?.toLong())
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI, it)
                }
                contentResolver.insert(ContactsContract.Data.CONTENT_URI, photoThumbnailContentValues)
            }
        }
    }

    suspend fun updateContact(contact: Contact) {
        withContext(ioDispatcher) {

            println("Updating Contact --> $contact")

            // Update the contact's display name
            val updateNameContentValues = ContentValues().apply {
                put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.name)
            }
            val updateNameSelection = "${ContactsContract.Data.CONTACT_ID} = ? AND " +
                    "${ContactsContract.Data.MIMETYPE} = ?"
            val updateNameSelectionArgs = arrayOf(
                contact.contactId,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            contentResolver.update(
                ContactsContract.Data.CONTENT_URI,
                updateNameContentValues,
                updateNameSelection,
                updateNameSelectionArgs
            )

            // Update the mobile number
            contact.mobile?.let {
                val updateMobileContentValues = ContentValues().apply {
                    put(ContactsContract.CommonDataKinds.Phone.NUMBER, it)
                }
                val updateMobileSelection = "${ContactsContract.Data.CONTACT_ID} = ? AND " +
                        "${ContactsContract.Data.MIMETYPE} = ? AND " +
                        "${ContactsContract.CommonDataKinds.Phone.TYPE} = ?"
                val updateMobileSelectionArgs = arrayOf(
                    contact.contactId,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE.toString()
                )
                contentResolver.update(
                    ContactsContract.Data.CONTENT_URI,
                    updateMobileContentValues,
                    updateMobileSelection,
                    updateMobileSelectionArgs
                )
            }

            // Update the phone number (other)
            contact.phone?.let {
                val updatePhoneContentValues = ContentValues().apply {
                    put(ContactsContract.CommonDataKinds.Phone.NUMBER, it)
                }
                val updatePhoneSelection = "${ContactsContract.Data.CONTACT_ID} = ? AND " +
                        "${ContactsContract.Data.MIMETYPE} = ? AND " +
                        "${ContactsContract.CommonDataKinds.Phone.TYPE} = ?"
                val updatePhoneSelectionArgs = arrayOf(
                    contact.contactId,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_OTHER.toString()
                )
                contentResolver.update(
                    ContactsContract.Data.CONTENT_URI,
                    updatePhoneContentValues,
                    updatePhoneSelection,
                    updatePhoneSelectionArgs
                )
            }

            // Update the email address
            contact.email?.let {
                val updateEmailContentValues = ContentValues().apply {
                    put(ContactsContract.CommonDataKinds.Email.ADDRESS, it)
                }
                val updateEmailSelection = "${ContactsContract.Data.CONTACT_ID} = ? AND " +
                        "${ContactsContract.Data.MIMETYPE} = ?"
                val updateEmailSelectionArgs = arrayOf(
                    contact.contactId,
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                )
                contentResolver.update(
                    ContactsContract.Data.CONTENT_URI,
                    updateEmailContentValues,
                    updateEmailSelection,
                    updateEmailSelectionArgs
                )
            }

            // Update the photo URI
            contact.photo?.let {
                val updatePhotoContentValues = ContentValues().apply {
                    put(ContactsContract.CommonDataKinds.Photo.PHOTO_URI, it)
                }
                val updatePhotoSelection = "${ContactsContract.Data.CONTACT_ID} = ? AND " +
                        "${ContactsContract.Data.MIMETYPE} = ?"
                val updatePhotoSelectionArgs = arrayOf(
                    contact.contactId,
                    ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                )
                contentResolver.update(
                    ContactsContract.Data.CONTENT_URI,
                    updatePhotoContentValues,
                    updatePhotoSelection,
                    updatePhotoSelectionArgs
                )
            }

            // Update the photo thumbnail URI
            contact.photoThumbnail?.let {
                val updatePhotoThumbnailContentValues = ContentValues().apply {
                    put(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI, it)
                }
                val updatePhotoThumbnailSelection = "${ContactsContract.Data.CONTACT_ID} = ? AND " +
                        "${ContactsContract.Data.MIMETYPE} = ?"
                val updatePhotoThumbnailSelectionArgs = arrayOf(
                    contact.contactId,
                    ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                )
                contentResolver.update(
                    ContactsContract.Data.CONTENT_URI,
                    updatePhotoThumbnailContentValues,
                    updatePhotoThumbnailSelection,
                    updatePhotoThumbnailSelectionArgs
                )
            }
        }
    }

    suspend fun deleteContact(contactId: String) {
        return withContext(ioDispatcher) {

            val ops = ArrayList<ContentProviderOperation>()
            ops.add(
                ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(
                        "${ContactsContract.RawContacts.CONTACT_ID}=?",
                        arrayOf(contactId)
                    )
                    .build()
            )

            try {
                contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    suspend fun getContactById(contactId: String): Contact {

        return withContext(ioDispatcher) {

            var contact = Contact()

            val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
            )
            val selection = "${ContactsContract.Contacts._ID} = ?"
            val selectionArgs = arrayOf(contactId)

            val contactCursor: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )

            contactCursor?.use { cursor ->
                if (cursor.moveToFirst()) {

                    val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    val photoThumbnailUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                    val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI))
                    val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0

                    val mobileNumbers = if(hasPhoneNumber) getMobileNumbersForContact(contactId = id.toLong()) else emptyList()
                    val email = getEmailForContact(contactId = id)

                    contact = Contact(
                        contactId = id,
                        name = displayName,
                        gender = null,
                        mobile = if (mobileNumbers.isNotEmpty()) mobileNumbers[0] else null,
                        phone = if (mobileNumbers.size > 1) mobileNumbers[1] else null,
                        email = if (email.isNullOrBlank()) null else email,
                        photoThumbnail = photoThumbnailUri,
                        photo = photoUri,
                        contactType = ContactType.LOCAL
                    )

                    println("Queried Contact --> $contact")

                }
            }

            contact

        }

    }

    suspend fun getAllContacts(): List<Contact> {
        return withContext(ioDispatcher) {

            val contacts = mutableListOf<Contact>()

            val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
            )
            val sortOrder = "${ContactsContract.Contacts.DISPLAY_NAME} ASC"

            val contactCursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            contactCursor?.use { cursor ->

                while (cursor.moveToNext()) {

                    val contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    val photoThumbnailUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                    val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI))
                    val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0

                    val mobileNumbers = if(hasPhoneNumber) getMobileNumbersForContact(contactId = contactId.toLong()) else emptyList()
                    val email = getEmailForContact(contactId = contactId)
                    println("Testing -> $mobileNumbers")

                    val contact = Contact(
                        contactId = contactId,
                        name = displayName,
                        gender = null,
                        mobile = if (mobileNumbers.isNotEmpty()) mobileNumbers[0] else null,
                        phone = if (mobileNumbers.size > 1) mobileNumbers[1] else null,
                        email = if (email.isNullOrBlank()) null else email,
                        photoThumbnail = photoThumbnailUri,
                        photo = photoUri,
                        contactType = ContactType.LOCAL
                    )

                    contacts.add(contact)

                }

            }

            println(contacts)

            contacts
        }
    }

    private fun getMobileNumbersForContact(contactId: Long): List<String> {
        // Define the projection to retrieve the phone number and its type
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE
        )

        // Define the selection criteria to retrieve mobile numbers for a specific contact ID
        val selection = (
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ? AND " +
                        "${ContactsContract.CommonDataKinds.Phone.MIMETYPE} = ?"
                )
        val selectionArgs = arrayOf(
            contactId.toString(),
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )

        // Perform the query
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        // Process the query results
        val numbers = mutableListOf<String>()
        cursor?.use {
            while (it.moveToNext()) {
                val number = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                if (number.isNotBlank()) numbers.add(number)
            }
        }
        cursor?.close()
        return numbers
    }

    private suspend fun getEmailForContact(contactId: String): String? {
        return withContext(ioDispatcher) {
            var email: String? = null
            val projection = arrayOf(ContactsContract.CommonDataKinds.Email.DATA)
            val selection =
                "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"
            val selectionArgs = arrayOf(contactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)

            val emailCursor: Cursor? = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )

            emailCursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    email = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA))
                }
            }

            email
        }
    }

    suspend fun upsertContact(contact: Contact) {
        contactsDao.upsertContacts(contacts = listOf(contact.toContactEntity()))
    }

    suspend fun deleteContactFromDatabaseByContactId(
        contactId: String
    ) {
        contactsDao.deleteContactById(contactId = contactId.toInt())
    }

    suspend fun getContactFromDatabaseById(contactId: String): Contact {
        return contactsDao.getContactById(contactId = contactId.toInt()).toContact()
    }

    fun observeContacts(): Flow<List<Contact>> {
        return contactsDao.observeContacts().map { contactsEntity ->
            contactsEntity.map { it.toContact() }
        }
    }

    fun getContactsByPage(): Flow<PagingData<ContactEntity>> = contactsPager.flow

}