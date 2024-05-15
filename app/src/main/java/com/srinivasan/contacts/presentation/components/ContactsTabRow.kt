package com.srinivasan.contacts.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.srinivasan.contacts.navigation.bottombar.TabItem

@Composable
fun ContactsTabRow(
    modifier: Modifier,
    tabItems: List<TabItem>,
    selectedTabIndex: Int,
    onClickTabItem: (Int) -> Unit
) {

    TabRow(
        modifier = modifier
            .fillMaxWidth(),
        selectedTabIndex = selectedTabIndex
    ) {

        tabItems.forEachIndexed { index, tabItem ->

            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onClickTabItem(index)
                },
                icon = { Icon(imageVector = tabItem.icon, contentDescription = tabItem.title) },
                text = {
                    Text(text = tabItem.title)
                }
            )

        }

    }

}