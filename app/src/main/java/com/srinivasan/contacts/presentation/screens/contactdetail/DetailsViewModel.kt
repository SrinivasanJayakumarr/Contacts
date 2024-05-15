package com.srinivasan.contacts.presentation.screens.contactdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srinivasan.contacts.data.repository.ContactsRepository
import com.srinivasan.contacts.di.module.IoDispatcher
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.domain.model.ContactType.*
import com.srinivasan.contacts.util.ContactsStateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


///////////////////////////////////////////////////////////////////////////////////////////////

// Created by Srinivasan Jayakumar on 09/May/2024 18:38

///////////////////////////////////////////////////////////////////////////////////////////////

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _contactDetails = MutableStateFlow<ContactsStateHandler<Contact>>(
        ContactsStateHandler.Loading()
    )
    val contactDetails = _contactDetails.asStateFlow()

    fun getContactsById(
        contactId: String,
        contactType: ContactType
    ) {
        viewModelScope.launch(ioDispatcher) {
            delay(500L)
            val details = when(contactType) {
                LOCAL -> {
                    contactsRepository.getContactById(contactId = contactId)
                }
                RANDOM -> {
                    contactsRepository.getContactFromDatabaseById(contactId = contactId)
                }
            }
            _contactDetails.emit(ContactsStateHandler.Success(details))
        }
    }

    fun deleteContact(
        contactId: String,
        contactType: ContactType
    ) {
        viewModelScope.launch(ioDispatcher) {
            if (contactType == LOCAL) {
                contactsRepository.deleteContact(contactId = contactId)
            } else {
                contactsRepository.deleteContactFromDatabaseByContactId(contactId = contactId)
            }
        }
    }

}