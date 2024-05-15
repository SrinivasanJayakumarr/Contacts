package com.srinivasan.contacts.presentation.screens.createoreditcontact.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.R
import com.srinivasan.contacts.presentation.components.ContactsTextField

@Composable
fun ContactNumbersComp(
    mobile: String,
    phone: String,
    onMobileChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.Top
    ) {

        Icon(
            modifier = Modifier
                .padding(10.dp),
            imageVector = Icons.Outlined.Call,
            contentDescription = stringResource(id = R.string.contact_number_icon)
        )

        Column {

            ContactsTextField(
                title = stringResource(id = R.string.mobile),
                value = mobile,
                onValueChange = onMobileChange,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                onNext = {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Down
                    )
                }
            )

            ContactsTextField(
                title = stringResource(id = R.string.phone),
                value = phone,
                onValueChange = onPhoneChange,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                onNext = {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Down
                    )
                }
            )

        }

    }

}