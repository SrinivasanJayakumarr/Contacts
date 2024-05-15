package com.srinivasan.contacts.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.R
import com.srinivasan.contacts.ui.theme.ContactsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarContacts(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    isClearVisible: Boolean,
    onClickClear: () -> Unit,
    onActiveChange: (Boolean) -> Unit,
    results: @Composable () -> Unit
) {

    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.outline
            )
        ),
        placeholder = {
            Text(text = stringResource(id = R.string.search_contacts))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(id = R.string.search_icon),
                modifier = Modifier.padding(start = 16.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        },
        trailingIcon = {
            if (isClearVisible) {
                Icon(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onClickClear() },
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = stringResource(id = R.string.clear_button),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        },
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange
    ) {
        results()
    }

}

@Preview(showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun SearchBarContactsPreview() {

    ContactsTheme {
        SearchBarContacts(
            query = "Sample",
            onQueryChange = {},
            onSearch = {},
            active = false,
            isClearVisible = true,
            onClickClear = { },
            onActiveChange = {}
        ) {

        }
    }

}