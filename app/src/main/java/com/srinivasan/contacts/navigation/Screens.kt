package com.srinivasan.contacts.navigation

import com.srinivasan.contacts.data.room.typeconvertors.Convertors
import com.srinivasan.contacts.domain.model.ContactType

const val CONTACT_ID = "contact_id"
const val CONTACT_TYPE = "contact_TYPE"
const val CONTACT_CREATE = "contact_create"

sealed class Screens(
    val route: String
) {

    data object Home: Screens(route = ScreenRoutes.HOME_SCREEN_ROUTE)
    data object Details: Screens(route = "${ScreenRoutes.DETAILS_SCREEN_ROUTE}/{$CONTACT_ID}/{$CONTACT_TYPE}") {
        fun passContact(
            contactId: String?,
            contactType: ContactType
        ): String {
            return "${ScreenRoutes.DETAILS_SCREEN_ROUTE}/$contactId/${Convertors().contactTypeToIntValue(contactType)}"
        }
    }
    data object CreateOrEdit: Screens(route = "${ScreenRoutes.CREATE_EDIT_SCREEN_ROUTE}/{$CONTACT_CREATE}/{$CONTACT_ID}/{$CONTACT_TYPE}") {
        fun passContact(
            createContact: Boolean,
            contactId: String?,
            contactType: ContactType
        ): String {
            return "${ScreenRoutes.CREATE_EDIT_SCREEN_ROUTE}/$createContact/$contactId/${Convertors().contactTypeToIntValue(contactType)}"
        }
    }

}

object ScreenRoutes {

    const val HOME_SCREEN_ROUTE = "home_screen"
    const val DETAILS_SCREEN_ROUTE = "details_screen"
    const val CREATE_EDIT_SCREEN_ROUTE = "create_edit_screen"

}