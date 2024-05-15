package com.srinivasan.contacts.presentation.screens.createoreditcontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srinivasan.contacts.data.repository.ContactsRepository
import com.srinivasan.contacts.di.module.IoDispatcher
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.util.ContactsStateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


///////////////////////////////////////////////////////////////////////////////////////////////

// Created by Srinivasan Jayakumar on 09/May/2024 18:37

///////////////////////////////////////////////////////////////////////////////////////////////

@HiltViewModel
class CreateOrEditViewModel @Inject constructor(
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
            val details = when(contactType) {
                ContactType.LOCAL -> {
                    contactsRepository.getContactById(contactId = contactId)
                }
                ContactType.RANDOM -> {
                    contactsRepository.getContactFromDatabaseById(contactId = contactId)
                }
            }
            _contactDetails.emit(ContactsStateHandler.Success(details))
        }
    }

    fun createNewContact(
        contact: Contact
    ) {
        viewModelScope.launch(ioDispatcher) {
            async {
                contactsRepository.createNewContact(contact = contact)
            }
            async {
                contactsRepository.upsertContact(contact = contact)
            }
        }
    }

    fun updateContact(
        contact: Contact,
        contactType: ContactType
    ) {
        viewModelScope.launch(ioDispatcher) {
            if (contactType == ContactType.LOCAL) {
                contactsRepository.updateContact(contact = contact)
            } else {
                contactsRepository.upsertContact(contact = contact)
            }
        }
    }


}