@file:OptIn(ExperimentalCoroutinesApi::class)

package com.ot.doggies.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ot.doggies.ui.BreedDetailScreen
import com.ot.doggies.ui.BreedFeedScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.collections.listOf

object NavigationArguments {

    const val breed = "breed"
    const val subBreed = "subBreed"
}

fun <T> SavedStateHandle.getOrThrow(argument: String): T =
    this[argument]
        ?: throw Exception("$argument not found in SavedStateHandle (you probably need to add to your navigation arguments)")

fun NavGraphBuilder.breedFeedScreen(navigator: Navigator) {
    composable(route = AppScreens.BreedFeedScreen.path) {
        BreedFeedScreen(onBreedClick = { breed, subBreed ->
            navigator.navigateToBreedDetail(
                breed = breed,
                subBreed = subBreed
            )
        })
    }
}

fun NavGraphBuilder.breedDetailScreen() {
    composable(
        route = "${AppScreens.BreedDetailScreen.path}/{breed}/{subBreed}",
        arguments = listOf(
            navArgument(NavigationArguments.breed) { type = NavType.StringType },
            navArgument(NavigationArguments.subBreed) { type = NavType.StringType },
        )
    ) {
        val breed = it.arguments?.getString(NavigationArguments.breed) ?: ""
        val subBreed = it.arguments?.getString(NavigationArguments.subBreed) ?: ""
        BreedDetailScreen(breed, subBreed)
    }
}