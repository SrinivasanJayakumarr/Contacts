package com.srinivasan.contacts.presentation.screens.contactdetail

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.srinivasan.contacts.R
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.navigation.Screens
import com.srinivasan.contacts.presentation.components.ContactAlertDialog
import com.srinivasan.contacts.presentation.components.ContactImage
import com.srinivasan.contacts.presentation.components.LeadingIconType
import com.srinivasan.contacts.presentation.components.TopAppBarContacts
import com.srinivasan.contacts.presentation.components.TopBarAction
import com.srinivasan.contacts.presentation.screens.contactdetail.components.ContactAction
import com.srinivasan.contacts.presentation.screens.contactdetail.components.ContactInfoCard
import com.srinivasan.contacts.presentation.screens.createoreditcontact.isVisible
import com.srinivasan.contacts.util.CommonUtil
import com.srinivasan.contacts.util.Constants.THRESHOLD
import com.srinivasan.contacts.util.ContactsStateHandler


///////////////////////////////////////////////////////////////////////////////////////////////

// Created by Srinivasan Jayakumar on 09/May/2024 18:36

///////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun DetailsScreen(
    navHostController: NavHostController,
    contactId: String?,
    contactType: ContactType,
    isLeadingIconVisible: Boolean = true,
    viewModel: DetailsViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var isNameVisible by remember {
        mutableStateOf(true)
    }

    var isDeleteDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = contactId) {
        contactId?.let {
            viewModel.getContactsById(
                contactId = it,
                contactType = contactType
            )
        }
    }

    if (isDeleteDialogVisible) {
        ContactAlertDialog(
            onConfirm = {
                contactId?.let {
                    viewModel.deleteContact(
                        contactId = it,
                        contactType = contactType
                    )
                }
                isDeleteDialogVisible = false
                navHostController.navigateUp()
            },
            onDismiss = {
                isDeleteDialogVisible = false
            }
        )
    }

    // Data
    val contactDetails by viewModel.contactDetails.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBarContacts(
                title = if (isNameVisible.not()) contactDetails.data?.name ?: "" else "",
                leadingIconType = LeadingIconType.BACK,
                isLeadingIconVisible = isLeadingIconVisible,
                onLeadingIconPressed = {
                    navHostController.navigateUp()
                },
                actions = {
                    TopBarAction(
                        icon = Icons.Outlined.Edit,
                        contentDescription = stringResource(id = R.string.edit_button),
                        onClick = {
                            navHostController.navigate(
                                Screens.CreateOrEdit.passContact(
                                    createContact = false,
                                    contactId = contactId,
                                    contactType = contactType
                                )
                            )
                        }
                    )

                    TopBarAction(
                        icon = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.delete_button),
                        onClick = {
                            isDeleteDialogVisible = true
                        }
                    )

                    TopBarAction(
                        icon = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(id = R.string.more_button),
                        onClick = {

                        }
                    )
                }
            )
        }
    ) { innerPadding ->

        when (contactDetails) {
            is ContactsStateHandler.Error -> {
                contactDetails.message?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    }
                }
            }

            is ContactsStateHandler.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()

                }
            }

            is ContactsStateHandler.Success -> {
                contactDetails.data?.let { contact ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ContactImage(
                            modifier = Modifier
                                .size(100.dp),
                            name = contact.name,
                            textSize = 50.sp,
                            imageUrl = contact.photo
                        )

                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .isVisible(
                                    threshold = THRESHOLD,
                                    onVisibilityChange = {
                                        isNameVisible = it
                                    }
                                ),
                            text = contact.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            ContactAction(
                                modifier = Modifier,
                                icon = Icons.Outlined.Call,
                                contentDescription = stringResource(id = R.string.contact_action_call),
                                onClick = {
                                    val callIntent = Intent(Intent.ACTION_DIAL)
                                    callIntent.data = Uri.parse("tel:${contact.mobile}")
                                    context.startActivity(callIntent)
                                }
                            )

                            ContactAction(
                                modifier = Modifier,
                                icon = Icons.Outlined.Sms,
                                contentDescription = stringResource(id = R.string.contact_action_message),
                                onClick = {
                                    contact.mobile?.let {
                                        CommonUtil.sendMessage(context = context, mobileNumber = it)
                                    }
                                }
                            )

                            ContactAction(
                                modifier = Modifier,
                                icon = Icons.Outlined.Videocam,
                                contentDescription = stringResource(id = R.string.contact_action_video_call),
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Not implemented",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )

                        }

                        ContactInfoCard(
                            contact = contact
                        )

                    }

                }
            }
        }

    }

}