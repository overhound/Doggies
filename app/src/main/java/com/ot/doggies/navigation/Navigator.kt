package com.ot.doggies.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import kotlin.sequences.any


class Navigator(private val navController: NavController) {

    @SuppressLint("RestrictedApi")
    fun isCurrentRoute(route: String): Boolean {
        return navController.currentDestination?.hierarchy
            ?.any { it.hasRoute(route = route, null) } == true
    }

    fun navigateToBreedDetail(breed: String, subBreed: String) =
        navController.navigate(route = "${AppScreens.BreedDetailScreen.path}/$breed/$subBreed")

}

enum class AppScreens(val path: String) {
    BreedFeedScreen(path = "BreedFeed"),
    BreedDetailScreen(path = "BreedDetail"),
}