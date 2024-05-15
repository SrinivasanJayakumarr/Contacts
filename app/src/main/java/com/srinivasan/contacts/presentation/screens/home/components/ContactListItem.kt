package com.srinivasan.contacts.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.presentation.components.ContactImage
import com.srinivasan.contacts.ui.theme.ContactsTheme

@Composable
fun ContactListItem(
    modifier: Modifier = Modifier,
    contact: Contact,
    isSelected: Boolean,
    onClick: (contactId: String?) -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) MaterialTheme.colorScheme
                    .primaryContainer
                    .copy(alpha = 0.5F)
                else Color.Unspecified
            )
            .clickable { onClick(contact.contactId) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        ContactImage(
            modifier = Modifier
                .size(35.dp),
            name = contact.name,
            imageUrl = contact.photoThumbnail
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = contact.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

    }

}

@Preview
@Composable
fun ContactListItemPreview() {

    ContactsTheme {
        ContactListItem(
            contact = Contact(
                contactId = "12345",
                name = "Sample",
                gender = null,
                photoThumbnail = null,
                photo = null,
                mobile = "3333333333",
                phone = null,
                contactType = ContactType.LOCAL
            ),
            isSelected = true,
            onClick = {}
        )
    }

}