package com.srinivasan.contacts.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.ui.theme.ContactsTheme

@Composable
fun ContactsTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onNext: (KeyboardActionScope) -> Unit = {},
    onDone: (KeyboardActionScope) -> Unit = {},
) {

    OutlinedTextField(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 5.dp),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = title) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext,
            onDone = onDone
        ),
        singleLine = singleLine
    )

}

@Preview(showBackground = true)
@Composable
fun ContactsTextFieldPreview() {

    ContactsTheme {
        ContactsTextField(
            title = "Test",
            value = "Sample",
            onValueChange = {},
            singleLine = true
        )
    }

}
