package com.srinivasan.contacts.data.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.srinivasan.contacts.data.room.dao.ContactsDao
import com.srinivasan.contacts.data.room.entity.ContactEntity
import com.srinivasan.contacts.data.room.typeconvertors.Convertors
import com.srinivasan.contacts.util.Constants.DATABASE_VERSION

@Database(
    entities = [ContactEntity::class],
    version = DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(Convertors::class)
abstract class ContactsDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactsDao

}