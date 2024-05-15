package com.srinivasan.contacts.util.mappers

import com.srinivasan.contacts.data.remote.ContactDetails
import com.srinivasan.contacts.data.room.entity.ContactEntity
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.domain.model.Gender
import com.srinivasan.contacts.util.CommonUtil.getProperMobileNumber
import com.srinivasan.contacts.util.Constants.API_RESPONSE_FEMALE
import com.srinivasan.contacts.util.Constants.API_RESPONSE_MALE
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun ContactDetails.toContactsEntity(): ContactEntity {

    return ContactEntity(
        contactId = id.value,
        name = name.first.plus(" ").plus(name.last),
        gender = when(gender){
            API_RESPONSE_MALE -> Gender.MALE
            API_RESPONSE_FEMALE -> Gender.FEMALE
            else -> Gender.MALE
        },
        mobile = cell.getProperMobileNumber(),
        phone = phone.getProperMobileNumber(),
        email = email,
        photoThumbnail = picture.thumbnail,
        photo = picture.large,
        contactType = ContactType.RANDOM
    )

}

fun ContactEntity.toContact(): Contact {
    return Contact(
        contactId = id.toString(),
        name = name,
        gender = gender,
        mobile = mobile?.ifBlank { null },
        phone = phone?.ifBlank { null },
        email = email?.ifBlank { null },
        photoThumbnail = photoThumbnail?.ifBlank { null },
        photo = photo?.ifBlank { null },
        contactType = contactType
    )
}

fun Contact.toContactEntity(): ContactEntity {
    return ContactEntity(
        id = contactId.trim().toInt(),
        contactId = contactId,
        name = name,
        gender = gender,
        mobile = mobile,
        phone = phone,
        email = email,
        photoThumbnail = photoThumbnail,
        photo = photo,
        contactType = contactType
    )
}

fun Contact.toJsonString(): String = Json.encodeToString(this)

fun jsonStringToContact(jsonString: String): Contact = Json.decodeFromString(jsonString)