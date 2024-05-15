package com.srinivasan.contacts.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.srinivasan.contacts.data.room.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Upsert
    suspend fun upsertContacts(contacts: List<ContactEntity>)

    @Query("SELECT * FROM Contacts ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, ContactEntity>

    @Query("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = \'Contacts\'")
    suspend fun resetPrimaryKeyAutoIncrementValue()

    @Query("SELECT * FROM Contacts WHERE id LIKE :contactId")
    suspend fun getContactById(contactId: Int): ContactEntity

    @Query("DELETE FROM Contacts")
    suspend fun deleteAllContacts()

    @Query("DELETE FROM Contacts WHERE id LIKE :contactId")
    suspend fun deleteContactById(contactId: Int)

    @Query("SELECT * FROM Contacts ORDER BY id DESC")
    fun observeContacts(): Flow<List<ContactEntity>>

}