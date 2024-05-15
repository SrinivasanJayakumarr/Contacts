package com.srinivasan.contacts.di.module

import android.content.ContentResolver
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.srinivasan.contacts.data.remote.ContactsApi
import com.srinivasan.contacts.data.remote.ContactsRemoteMediator
import com.srinivasan.contacts.data.repository.ContactsRepository
import com.srinivasan.contacts.data.room.dao.ContactsDao
import com.srinivasan.contacts.data.room.db.ContactsDatabase
import com.srinivasan.contacts.data.room.entity.ContactEntity
import com.srinivasan.contacts.util.Constants.PAGE_SIZE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun provideContactsRepository(
        contentResolver: ContentResolver,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        contactsPager: Pager<Int, ContactEntity>,
        contactsDao: ContactsDao
    ): ContactsRepository = ContactsRepository(
        contentResolver = contentResolver,
        ioDispatcher = ioDispatcher,
        contactsPager = contactsPager,
        contactsDao = contactsDao
    )


    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideContactsPager(
        contactsDatabase: ContactsDatabase,
        contactsApi: ContactsApi
    ): Pager<Int, ContactEntity> {

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = ContactsRemoteMediator(
                contactsDatabase = contactsDatabase,
                contactsApi = contactsApi
            ),
            pagingSourceFactory = {
                contactsDatabase.contactDao().pagingSource()
            }
        )

    }


}