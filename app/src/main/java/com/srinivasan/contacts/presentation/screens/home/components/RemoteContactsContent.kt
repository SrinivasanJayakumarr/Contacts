package com.srinivasan.contacts.presentation.screens.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.srinivasan.contacts.domain.model.Contact
import com.srinivasan.contacts.util.CommonUtil

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemoteContactsContent(
    modifier: Modifier = Modifier,
    contacts: LazyPagingItems<Contact>,
    selectedIndex: Int,
    onClick: (index: Int, contact: Contact) -> Unit,
    onError: (error: String) -> Unit
) {

    LaunchedEffect(key1 = contacts.loadState) {
        if(contacts.loadState.refresh is LoadState.Error) {
            val message = (contacts.loadState.refresh as LoadState.Error).error.message
            message?.let {
                onError(CommonUtil.getErrorMessage(message = it))
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        items(contacts.itemCount) { index ->

            if (contacts.itemCount > 0) {
                contacts[index]?.also { contact ->
                    ContactListItem(
                        modifier = Modifier
                            .animateItemPlacement(),
                        contact = contact,
                        isSelected = selectedIndex == index,
                        onClick = {
                            onClick(index, contact)
                        }
                    )
                }
            }

        }

        item {
            if (contacts.loadState.append is LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()

                }
            }
        }

    }

}