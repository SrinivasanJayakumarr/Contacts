package com.srinivasan.contacts.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.srinivasan.contacts.data.repository.ContactsRepository
import com.srinivasan.contacts.di.module.IoDispatcher
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.util.mappers.toContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


///////////////////////////////////////////////////////////////////////////////////////////////

// Created by Srinivasan Jayakumar on 09/May/2024 18:37

///////////////////////////////////////////////////////////////////////////////////////////////

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact = _selectedContact.asStateFlow()

    private val _searchContacts = MutableStateFlow<List<Contact>>(emptyList())

    @OptIn(FlowPreview::class)
    val searchResult = _searchText
        .debounce(500L)
        .combine(_searchContacts) { text, contacts ->
            if (text.isBlank()) {
                contacts
            } else {
                contacts.filter { it.name.contains(text, ignoreCase = true) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _contacts.value
        )

    val remoteContacts = contactsRepository
        .getContactsByPage()
        .map { pagingData ->
            pagingData.map { it.toContact() }
        }
        .cachedIn(viewModelScope)

    fun initializeLocalContacts() {
        viewModelScope.launch(ioDispatcher) {
            _searchContacts.emit(
                contactsRepository.observeContacts().first()
                + contactsRepository.getAllContacts()
            )
        }
    }

    fun onSelectContact(contact: Contact) {
        _selectedContact.value = contact
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        println("onSearchTextChange --> $text")
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    fun getAllContacts() {
        viewModelScope.launch(ioDispatcher) {
            _contacts.emit(contactsRepository.getAllContacts())
        }
    }

}