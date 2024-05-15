package com.srinivasan.contacts.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.srinivasan.contacts.data.room.db.ContactsDatabase
import com.srinivasan.contacts.data.room.entity.ContactEntity
import com.srinivasan.contacts.util.mappers.toContactsEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ContactsRemoteMediator @Inject constructor(
    private val contactsDatabase: ContactsDatabase,
    private val contactsApi: ContactsApi
) : RemoteMediator<Int, ContactEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ContactEntity>
    ): MediatorResult {

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val contactsResponse = contactsApi.getContactsPaging(
                page = loadKey,
                results = state.config.pageSize
            )

            println("Api Call Made --> Response --> $contactsResponse")

            contactsDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    contactsDatabase.contactDao().deleteAllContacts()
                    contactsDatabase.contactDao().resetPrimaryKeyAutoIncrementValue()
                }
                val contactEntities = contactsResponse.contacts.map { it.toContactsEntity() }
                contactsDatabase.contactDao().upsertContacts(contactEntities)

            }

            MediatorResult.Success(
                endOfPaginationReached = contactsResponse.contacts.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}