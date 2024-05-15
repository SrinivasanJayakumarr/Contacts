package com.srinivasan.contacts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.srinivasan.contacts.data.room.typeconvertors.Convertors
import com.srinivasan.contacts.presentation.screens.contactdetail.DetailsScreen
import com.srinivasan.contacts.presentation.screens.createoreditcontact.CreateOrEditScreen
import com.srinivasan.contacts.presentation.screens.home.HomeScreen

@Composable
fun RootNavGraph(
    navHostController: NavHostController
) {

    NavHost(
        navController = navHostController,
        startDestination = Screens.Home.route
    ) {

        composable(
            route = Screens.Home.route
        ) {
            HomeScreen(
                navHostController = navHostController
            )
        }

        composable(
            route = Screens.Details.route,
            arguments = listOf(
                navArgument(CONTACT_ID) { type = NavType.StringType },
                navArgument(CONTACT_TYPE) { type = NavType.IntType },
            )
        ) {

            DetailsScreen(
                navHostController = navHostController,
                contactId = it.arguments?.getString(CONTACT_ID),
                contactType = Convertors().fromIntValueToContactType(
                    it.arguments?.getInt(
                        CONTACT_TYPE
                    )!!
                )
            )
        }

        composable(
            route = Screens.CreateOrEdit.route,
            arguments = listOf(
                navArgument(CONTACT_CREATE) { type = NavType.BoolType },
                navArgument(CONTACT_ID) { type = NavType.StringType },
                navArgument(CONTACT_TYPE) { type = NavType.IntType },
            )
        ) {

            CreateOrEditScreen(
                createContact = it.arguments?.getBoolean(CONTACT_CREATE)!!,
                contactId = it.arguments?.getString(CONTACT_ID),
                contactType = Convertors().fromIntValueToContactType(
                    it.arguments?.getInt(
                        CONTACT_TYPE
                    )!!
                ),
                onClickLeadingIcon = {
                    navHostController.navigateUp()
                }
            )
        }

    }

}