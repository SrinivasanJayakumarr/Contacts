package com.srinivasan.contacts.presentation.screens.contactdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.R
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.domain.model.Gender.FEMALE
import com.srinivasan.contacts.domain.model.Gender.MALE
import com.srinivasan.contacts.ui.theme.ContactsTheme
import com.srinivasan.contacts.util.CommonUtil

@Composable
fun ContactInfoCard(
    modifier: Modifier = Modifier,
    contact: Contact
) {

    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
        ) {

            Text(
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 20.dp),
                text = stringResource(id = R.string.contact_info),
                style = MaterialTheme.typography.titleLarge
            )

            contact.mobile?.let {
                ContactInfoItem(
                    icon = Icons.Outlined.Call,
                    title = it,
                    value = stringResource(id = R.string.mobile),
                    onClick = {
                        CommonUtil.makeCall(context = context, mobileNumber = it)
                    }
                )
            }

            contact.phone?.let {
                ContactInfoItem(
                    icon = Icons.Outlined.Call,
                    title = it,
                    value = stringResource(id = R.string.home),
                    onClick = {
                        CommonUtil.makeCall(context = context, mobileNumber = it)
                    }
                )
            }

            contact.email?.let {
                ContactInfoItem(
                    icon = Icons.Outlined.Mail,
                    title = it,
                    value = stringResource(id = R.string.email),
                    onClick = {
                        CommonUtil.sendMail(context = context, recipientEmail = it)
                    }
                )
            }

            contact.gender?.let {
                ContactInfoItem(
                    icon = when(it) {
                        MALE -> Icons.Outlined.Male
                        FEMALE -> Icons.Outlined.Female
                    },
                    title = it.name,
                    value = stringResource(id = R.string.gender),
                    onClick = {

                    }
                )
            }

        }

    }

}

@Composable
fun ContactInfoItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            imageVector = icon,
            contentDescription = "Icon"
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium
            )

        }

    }

}

@Preview
@Composable
fun ContactInfoItemPreview() {
    ContactsTheme {
        ContactInfoItem(
            icon = Icons.Outlined.Call,
            title = "8888888888",
            value = "Mobile",
            onClick = {

            }
        )
    }
}

@Preview
@Composable
fun ContactInfoCardPreview() {

    ContactsTheme {
        ContactInfoCard(
            contact = Contact()
        )
    }

}