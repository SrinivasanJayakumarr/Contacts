package com.srinivasan.contacts.presentation.screens.contactdetail.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.srinivasan.contacts.ui.theme.ContactsTheme

@Composable
fun ContactAction(
    modifier: Modifier,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {

    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
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
fun ContactActionPreview() {
    ContactsTheme {
        ContactAction(
            modifier = Modifier,
            icon = Icons.Outlined.Call,
            contentDescription = ""
        ) {

        }
    }
}