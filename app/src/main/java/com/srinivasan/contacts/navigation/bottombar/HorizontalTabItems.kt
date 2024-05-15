package com.srinivasan.contacts.navigation.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TabItem(
    val icon: ImageVector,
    val title: String,
) {

    data object THIS_DEVICE: TabItem(icon = Icons.Rounded.Smartphone, title = "This Device")
    data object RANDOM: TabItem(icon = Icons.Rounded.Language, title = "Random")

}