package com.srinivasan.contacts.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.srinivasan.contacts.R
import com.srinivasan.contacts.ui.theme.ContactsTheme

@Composable
fun TopBarAction(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {

    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

}

@Preview
@Composable
fun TopBarActionPreview() {
    ContactsTheme {
        TopBarAction(
            icon = Icons.Rounded.Edit,
            contentDescription = stringResource(id = R.string.edit_button),
            onClick = {}
        )
    }
}