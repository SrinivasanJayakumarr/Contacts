package com.srinivasan.contacts.di.module

import android.content.Context
import androidx.room.Room
import com.srinivasan.contacts.data.room.dao.ContactsDao
import com.srinivasan.contacts.data.room.db.ContactsDatabase
import com.srinivasan.contacts.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContactsDatabase(
        @ApplicationContext context: Context
    ): ContactsDatabase {
        return Room.databaseBuilder(
            context,
            ContactsDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideContactsDao(
        contactsDatabase: ContactsDatabase
    ): ContactsDao = contactsDatabase.contactDao()

}