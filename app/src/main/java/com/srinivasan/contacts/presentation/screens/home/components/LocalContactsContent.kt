package com.srinivasan.contacts.presentation.screens.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.srinivasan.contacts.domain.model.Contact

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocalContactsContent(
    modifier: Modifier = Modifier,
    contacts: List<Contact>,
    selectedIndex: Int,
    onClick: (index: Int, contact: Contact) -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        itemsIndexed(
            items = contacts
        ) { index, contact ->

            ContactListItem(
                modifier = Modifier
                    .animateItemPlacement(),
                contact = contact,
                isSelected = selectedIndex == index,
                onClick = {
                    onClick(index, contact)
                    println("Testing --> $contact")
                }
            )

        }

    }

}