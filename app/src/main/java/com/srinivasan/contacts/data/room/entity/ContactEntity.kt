package com.srinivasan.contacts.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.domain.model.Gender

@Entity(tableName = "Contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val contactId: String?,
    val name: String,
    val gender: Gender?,
    val mobile: String?,
    val phone: String?,
    val email: String?,
    val photoThumbnail: String?,
    val photo: String?,
    val contactType: ContactType = ContactType.RANDOM
)
