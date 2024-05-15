package com.srinivasan.contacts.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.srinivasan.contacts.R
import com.srinivasan.contacts.ui.theme.ContactsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContacts(
    title: String,
    leadingIconType: LeadingIconType = LeadingIconType.BACK,
    isLeadingIconVisible: Boolean = false,
    onLeadingIconPressed: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        navigationIcon = {
            if (isLeadingIconVisible) {
                IconButton(
                    onClick = onLeadingIconPressed
                ) {
                    Icon(
                        imageVector = when (leadingIconType) {
                            LeadingIconType.BACK -> Icons.AutoMirrored.Rounded.ArrowBack
                            LeadingIconType.CANCEL -> Icons.Rounded.Close
                        },
                        contentDescription = when (leadingIconType) {
                            LeadingIconType.BACK -> stringResource(id = R.string.back_button)
                            LeadingIconType.CANCEL -> stringResource(id = R.string.close_button)
                        },
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            actions()
        }
    )

}

@Preview
@Composable
fun TopAppBarContactsPreview() {
    ContactsTheme {
        TopAppBarContacts(
            title = "Contacts",
            leadingIconType = LeadingIconType.CANCEL,
            isLeadingIconVisible = true,
            onLeadingIconPressed = { },
            actions = {
                TopBarAction(
                    icon = Icons.Rounded.Edit,
                    contentDescription = stringResource(id = R.string.edit_button),
                    onClick = {

                    }
                )

                TopBarAction(
                    icon = Icons.Rounded.Delete,
                    contentDescription = stringResource(id = R.string.delete_button),
                    onClick = {

                    }
                )

                TopBarAction(
                    icon = Icons.Rounded.MoreVert,
                    contentDescription = stringResource(id = R.string.delete_button),
                    onClick = {

                    }
                )
            }
        )
    }
}

enum class LeadingIconType {
    BACK,
    CANCEL
}