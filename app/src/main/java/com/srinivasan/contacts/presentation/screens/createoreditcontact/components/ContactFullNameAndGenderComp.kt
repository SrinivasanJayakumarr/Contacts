package com.srinivasan.contacts.presentation.screens.createoreditcontact.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.R
import com.srinivasan.contacts.domain.model.ContactType
import com.srinivasan.contacts.domain.model.Gender
import com.srinivasan.contacts.presentation.components.ContactsTextField
import com.srinivasan.contacts.ui.theme.ContactsTheme


@Composable
fun ContactFullNameAndGenderComp(
    contactType: ContactType,
    firstName: String,
    lastName: String,
    selectedGender: Gender,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onSelectGender: (Gender) -> Unit,
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
            imageVector = Icons.Outlined.Person,
            contentDescription = stringResource(id = R.string.full_name_icon)
        )

        Column {

            ContactsTextField(
                title = stringResource(id = R.string.first_name),
                value = firstName,
                onValueChange = onFirstNameChange,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onNext = {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Down
                    )
                }
            )

            ContactsTextField(
                title = stringResource(id = R.string.last_name),
                value = lastName,
                onValueChange = onLastNameChange,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onNext = {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Down
                    )
                }
            )

            if (contactType == ContactType.RANDOM) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {

                    OutlinedButton(
                        colors = if (selectedGender == Gender.MALE)
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        else ButtonDefaults.outlinedButtonColors(),
                        shape = CircleShape,
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onSelectGender(Gender.MALE)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Male,
                            contentDescription = stringResource(id = R.string.male_icon)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        colors = if (selectedGender == Gender.FEMALE)
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        else ButtonDefaults.outlinedButtonColors(),
                        shape = CircleShape,
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onSelectGender(Gender.FEMALE)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Female,
                            contentDescription = stringResource(id = R.string.female_icon)
                        )
                    }

                }
            }

        }

    }

}

@Preview
@Composable
fun ContactFullNameCompPreview() {
    ContactsTheme {
        ContactFullNameAndGenderComp(
            contactType = ContactType.RANDOM,
            firstName = "",
            lastName = "",
            selectedGender = Gender.MALE,
            onFirstNameChange = {},
            onLastNameChange = {},
            onSelectGender = {}
        )
    }
}