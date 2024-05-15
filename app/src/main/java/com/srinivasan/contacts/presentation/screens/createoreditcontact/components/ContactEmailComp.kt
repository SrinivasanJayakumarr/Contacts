package com.srinivasan.contacts.presentation.screens.createoreditcontact.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.R
import com.srinivasan.contacts.presentation.components.ContactsTextField

@Composable
fun ContactEmailComp(
    email: String,
    onEmailChange: (String) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.Top
    ) {

        Icon(
            modifier = Modifier
                .padding(10.dp),
            imageVector = Icons.Outlined.Mail,
            contentDescription = stringResource(id = R.string.mail_icon)
        )

        ContactsTextField(
            title = stringResource(id = R.string.email_without_dash),
            value = email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )

    }

}