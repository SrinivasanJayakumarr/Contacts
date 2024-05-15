package com.srinivasan.contacts.presentation.screens.createoreditcontact

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.srinivasan.contacts.R
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.domain.model.Gender
import com.srinivasan.contacts.presentation.components.LeadingIconType
import com.srinivasan.contacts.presentation.components.ProfileImage
import com.srinivasan.contacts.presentation.components.TopAppBarContacts
import com.srinivasan.contacts.presentation.screens.createoreditcontact.components.ContactEmailComp
import com.srinivasan.contacts.presentation.screens.createoreditcontact.components.ContactFullNameAndGenderComp
import com.srinivasan.contacts.presentation.screens.createoreditcontact.components.ContactNumbersComp
import com.srinivasan.contacts.util.CommonUtil
import com.srinivasan.contacts.util.ContactsStateHandler
import kotlinx.coroutines.launch


///////////////////////////////////////////////////////////////////////////////////////////////

// Created by Srinivasan Jayakumar on 09/May/2024 18:36

///////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun CreateOrEditScreen(
    createContact: Boolean,
    contactId: String?,
    contactType: ContactType,
    onClickLeadingIcon: () -> Unit,
    viewModel: CreateOrEditViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    // Data
    val contactDetails by viewModel.contactDetails.collectAsStateWithLifecycle()

    var firstName by rememberSaveable {
        mutableStateOf("")
    }

    var lastName by rememberSaveable {
        mutableStateOf("")
    }

    var gender by rememberSaveable {
        mutableStateOf(Gender.MALE)
    }

    var mobile by rememberSaveable {
        mutableStateOf("")
    }

    var phone by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var photoUri by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var photoThumnailUri by rememberSaveable {
        mutableStateOf("")
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //When the user has selected a photo, its URI is returned here
            println("Photo Uri --> $uri")
            photoUri = uri.toString()
        }

    LaunchedEffect(key1 = true) {

        if (createContact.not()) {
            contactId?.let {
                viewModel.getContactsById(contactId = contactId, contactType = contactType)
            }
        }

    }

    LaunchedEffect(key1 = contactDetails) {
        if (
            contactDetails is ContactsStateHandler.Success
            && createContact.not()
        ) {
            contactDetails.data?.let { contact ->
                val name = contact.name.split(" ")
                if (name.size > 1) {
                    firstName = name.first()
                    lastName = name.last()
                } else firstName = name.first()

                contact.gender?.let {
                    gender = it
                }

                contact.mobile?.let {
                    mobile = it
                }

                contact.phone?.let {
                    phone = it
                }

                contact.email?.let {
                    email = it
                }

                contact.photo?.let {
                    photoUri = it
                }

                contact.photoThumbnail?.let {
                    photoThumnailUri = it
                }

            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarContacts(
                title = if (createContact) stringResource(id = R.string.create_contact) else stringResource(
                    id = R.string.edit_contact
                ),
                leadingIconType = LeadingIconType.CANCEL,
                isLeadingIconVisible = true,
                onLeadingIconPressed = onClickLeadingIcon,
                actions = {
                    TextButton(
                        onClick = {

                            val contactFromFields = Contact(
                                contactId = if (createContact) "0" else contactId ?: "0",
                                name = firstName.plus(" ").plus(lastName),
                                mobile = mobile,
                                phone = phone,
                                email = email,
                                gender = if (contactType == ContactType.RANDOM) gender else null,
                                photo = photoUri ?: "",
                                photoThumbnail = photoUri,
                                contactType = contactType
                            )

                            if (createContact) {
                                if (CommonUtil.checkIfContactValid(contact = contactFromFields)) {
                                    viewModel.createNewContact(
                                        contact = CommonUtil.getProperContact(
                                            contact = contactFromFields
                                        )
                                    )
                                    onClickLeadingIcon()
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = context.getString(R.string.invalid_contact_data)
                                        )
                                    }
                                }
                            }
                            else {
                                if (CommonUtil.checkIfContactValid(contact = contactFromFields)) {
                                    viewModel.updateContact(
                                        contact = CommonUtil.getProperContact(
                                            contact = contactFromFields
                                        ),
                                        contactType = contactType
                                    )
                                    onClickLeadingIcon()
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = context.getString(R.string.invalid_contact_data)
                                        )
                                    }
                                }
                            }
                        }
                    ) {

                        Text(
                            modifier = Modifier
                                .padding(5.dp),
                            text = stringResource(id = R.string.save)
                        )

                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ProfileImage(
                modifier = Modifier
                    .size(100.dp),
                iconSize = 50.dp,
                photoUri = photoUri,
                onChange = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onRemove = {
                    photoUri = null
                }
            )

            ContactFullNameAndGenderComp(
                contactType = contactType,
                firstName = firstName,
                lastName = lastName,
                onFirstNameChange = {
                    firstName = it
                },
                onLastNameChange = {
                    lastName = it
                },
                selectedGender = gender,
                onSelectGender = {
                    gender = it
                }
            )

            ContactNumbersComp(
                mobile = mobile,
                phone = phone,
                onMobileChange = {
                    mobile = it
                },
                onPhoneChange = {
                    phone = it
                }
            )

            ContactEmailComp(
                email = email,
                onEmailChange = {
                    email = it
                }
            )

        }

    }

}


fun Modifier.isVisible(
    threshold: Int,
    onVisibilityChange: (Boolean) -> Unit
) = composed {

    Modifier.onGloballyPositioned { layoutCoordinates: LayoutCoordinates ->
        val layoutHeight = layoutCoordinates.size.height
        val thresholdHeight = layoutHeight * threshold / 100
        val layoutTop = layoutCoordinates.positionInRoot().y
        val layoutBottom = layoutTop + layoutHeight

        // This should be parentLayoutCoordinates not parentCoordinates
        val parent = layoutCoordinates.parentLayoutCoordinates

        parent?.boundsInRoot()?.let { rect: Rect ->
            val parentTop = rect.top
            val parentBottom = rect.bottom

            if (
                parentBottom - layoutTop > thresholdHeight &&
                (parentTop < layoutBottom - thresholdHeight)
            ) {
                onVisibilityChange(true)
            } else {
                onVisibilityChange(false)

            }
        }
    }
}