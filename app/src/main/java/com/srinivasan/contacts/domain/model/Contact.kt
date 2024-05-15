package com.srinivasan.contacts.domain.model

data class Contact(
    val contactId: String = "0",
    val name: String = "Test",
    val gender: Gender? = null,
    val mobile: String? = "9876543210",
    val phone: String? = "4312560101",
    val email: String? = "sample@mail.com",
    val photoThumbnail: String? = null,
    val photo: String? = null,
    val contactType: ContactType = ContactType.LOCAL
)

enum class Gender {
    MALE,
    FEMALE
}

enum class ContactType {
    LOCAL,
    RANDOM
}
