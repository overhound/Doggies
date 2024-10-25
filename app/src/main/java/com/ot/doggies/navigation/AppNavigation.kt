package com.ot.doggies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MainNavigation(
    navController: NavHostController
) {
    val navigator = remember { Navigator(navController) }
    NavHost(navController = navController, startDestination = AppScreens.BreedFeedScreen.path) {
        breedFeedScreen(navigator)
        breedDetailScreen()
    }

}