package com.srinivasan.contacts.presentation.screens.home

import android.Manifest
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowWidthSizeClass
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.srinivasan.contacts.R
import com.srinivasan.contacts.domain.model.ContactType.RANDOM
import com.srinivasan.contacts.navigation.Screens
import com.srinivasan.contacts.navigation.bottombar.TabItem
import com.srinivasan.contacts.presentation.components.ContactsTabRow
import com.srinivasan.contacts.presentation.components.SearchBarContacts
import com.srinivasan.contacts.presentation.screens.contactdetail.DetailsScreen
import com.srinivasan.contacts.presentation.screens.createoreditcontact.CreateOrEditScreen
import com.srinivasan.contacts.presentation.screens.home.components.ContactListItem
import com.srinivasan.contacts.presentation.screens.home.components.LocalContactsContent
import com.srinivasan.contacts.presentation.screens.home.components.RemoteContactsContent
import kotlinx.coroutines.launch


///////////////////////////////////////////////////////////////////////////////////////////////

// Created by Srinivasan Jayakumar on 09/May/2024 18:36

///////////////////////////////////////////////////////////////////////////////////////////////

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    var isCreateContactScreenVisible by remember {
        mutableStateOf(false)
    }

    val isDetailsScreenVisible by remember(isCreateContactScreenVisible) {
        mutableStateOf(
            windowSize.windowWidthSizeClass != WindowWidthSizeClass.COMPACT
                    && configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                    && isCreateContactScreenVisible.not()
        )
    }

    var selectedContactIndex by rememberSaveable {
        mutableIntStateOf(-1)
    }

    val contactsPermissions = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
        onPermissionsResult = {
            viewModel.getAllContacts()
            viewModel.initializeLocalContacts()
            Toast.makeText(
                context,
                "Permission Allowed",
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    LaunchedEffect(key1 = true) {
        if (contactsPermissions.allPermissionsGranted.not()) {
            contactsPermissions.launchMultiplePermissionRequest()
        } else {
            viewModel.getAllContacts()
            viewModel.initializeLocalContacts()
        }
    }

    // Data
    val localContacts by viewModel.contacts.collectAsStateWithLifecycle()
    val remoteContacts = viewModel.remoteContacts.collectAsLazyPagingItems()
    val selectedContact by viewModel.selectedContact.collectAsStateWithLifecycle()

    // Search Data
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = isCreateContactScreenVisible.not(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {

                FloatingActionButton(
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        if (windowSize.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                            isCreateContactScreenVisible = true
                        } else {
                            navHostController.navigate(
                                Screens.CreateOrEdit.passContact(
                                    createContact = true,
                                    contactId = "0",
                                    contactType = RANDOM
                                )
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PersonAdd,
                        contentDescription = stringResource(id = R.string.fab_icon)
                    )
                }

            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F)
            ) {

                SearchBarContacts(
                    query = searchText,
                    onQueryChange = viewModel::onSearchTextChange,
                    onSearch = viewModel::onSearchTextChange,
                    active = isSearching,
                    isClearVisible = searchText.isNotBlank(),
                    onClickClear = { viewModel.onSearchTextChange("") },
                    onActiveChange = { viewModel.onToggleSearch() }
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        searchResult.forEach { contact ->
                            item {
                                ContactListItem(
                                    modifier = Modifier
                                        .weight(0.9F),
                                    contact = contact,
                                    isSelected = false,
                                ) {
                                    viewModel.onSearchTextChange(contact.name)
                                    if (isDetailsScreenVisible) {
                                        viewModel.onSelectContact(contact = contact)
                                    } else {
                                        navHostController.navigate(
                                            Screens.Details.passContact(
                                                contactId = contact.contactId,
                                                contactType = contact.contactType
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        if (searchResult.isEmpty()) {
                            item {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp),
                                    text = stringResource(id = R.string.no_results_found),
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    }

                }

                ContactsTabRow(
                    modifier = Modifier,
                    tabItems = listOf(TabItem.THIS_DEVICE, TabItem.RANDOM),
                    selectedTabIndex = selectedItemIndex,
                    onClickTabItem = { index ->
                        selectedItemIndex = index
                    }
                )

                when (selectedItemIndex) {
                    0 -> {
                        LocalContactsContent(
                            modifier = Modifier,
                            contacts = localContacts,
                            selectedIndex = selectedContactIndex,
                            onClick = { index, contact ->
                                if (isDetailsScreenVisible) {
                                    viewModel.onSelectContact(contact)
                                    selectedContactIndex = index
                                } else {
                                    navHostController.navigate(
                                        Screens.Details.passContact(
                                            contactId = contact.contactId,
                                            contactType = contact.contactType
                                        )
                                    )
                                }
                            }
                        )
                    }

                    else -> {
                        RemoteContactsContent(
                            modifier = Modifier,
                            contacts = remoteContacts,
                            selectedIndex = selectedContactIndex,
                            onClick = { index, contact ->
                                if (isDetailsScreenVisible) {
                                    viewModel.onSelectContact(contact)
                                    selectedContactIndex = index
                                } else {
                                    navHostController.navigate(
                                        Screens.Details.passContact(
                                            contactId = contact.contactId,
                                            contactType = contact.contactType
                                        )
                                    )
                                }
                            },
                            onError = { error ->
                                scope.launch {
                                    snackBarHostState.showSnackbar(error)
                                }
                            }
                        )
                    }
                }

            }

            if (isDetailsScreenVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1F),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    selectedContact?.let {

                        println("SelectedContact --> $it")

                        DetailsScreen(
                            navHostController = navHostController,
                            contactId = it.contactId,
                            contactType = it.contactType,
                            isLeadingIconVisible = false
                        )

                    } ?: kotlin.run {
                        Text(text = stringResource(id = R.string.select_contact_to_display))
                    }

                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F),
                visible = isCreateContactScreenVisible,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1F),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CreateOrEditScreen(
                        createContact = true,
                        contactId = "0",
                        contactType = RANDOM,
                        onClickLeadingIcon = {
                            isCreateContactScreenVisible = false
                        }
                    )

                }

            }

        }

    }


}