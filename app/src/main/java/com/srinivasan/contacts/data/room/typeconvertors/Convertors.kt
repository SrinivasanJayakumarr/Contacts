package com.srinivasan.contacts.data.room.typeconvertors

import androidx.room.TypeConverter
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.domain.model.Gender

class Convertors {

    @TypeConverter
    fun fromIntValueToGender(value: Int): Gender {
        return when(value){
            0 -> Gender.MALE
            else -> Gender.FEMALE
        }
    }

    @TypeConverter
    fun genderToIntValue(gender: Gender): Int {
        return when(gender){
            Gender.MALE -> 0
            Gender.FEMALE -> 1
        }
    }

    @TypeConverter
    fun fromIntValueToContactType(value: Int): ContactType {
        return when(value) {
            0 -> ContactType.LOCAL
            else -> ContactType.RANDOM
        }
    }

    @TypeConverter
    fun contactTypeToIntValue(contactType: ContactType): Int {
        return when(contactType) {
            ContactType.LOCAL -> 0
            ContactType.RANDOM -> 1
        }
    }

}