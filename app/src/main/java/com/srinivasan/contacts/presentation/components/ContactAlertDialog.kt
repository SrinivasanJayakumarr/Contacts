package com.srinivasan.contacts.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.R


///////////////////////////////////////////////////////////////////////////////////////////////

// Created by Srinivasan Jayakumar on 15/May/2024 15:30

///////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun ContactAlertDialog(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.delete_contact),
    subTitle: String = stringResource(id = R.string.delete_message),
    icon: ImageVector = Icons.Outlined.Delete,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        modifier = modifier,
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        icon = {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                imageVector = icon,
                contentDescription = "Delete Contact Icon"
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
    )

}