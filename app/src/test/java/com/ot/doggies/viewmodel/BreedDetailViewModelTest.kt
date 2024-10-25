package com.ot.doggies.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ot.doggies.CoroutineTestRule
import com.ot.doggies.data.NetworkResult
import com.ot.doggies.repositories.BreedRepository
import com.ot.doggies.ui.viewmodel.BreedFeedViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BreedFeedViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val breedRepository = mockk<BreedRepository>()
    private val dispatcherProvider = coroutineTestRule.testDispatcherProvider

    private lateinit var viewModel: BreedFeedViewModel

    private fun initViewModel() {
        viewModel = BreedFeedViewModel(breedRepository, dispatcherProvider)
    }

    @Test
    fun `init should update uiState with breeds on success`() = runTest {
        val breeds = mapOf(
            "Hound" to listOf("Afghan", "Basset"),
            "Retriever" to listOf("Golden", "Labrador")
        )

        coEvery { breedRepository.getAllBreeds() } returns NetworkResult.Success(breeds)

        initViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(
                BreedFeedViewModel.UiState(breeds = breeds)
            )
        }
    }

    @Test
    fun `init should update uiState with error on failure`() = runTest {
        val errorMessage = "Failed to fetch breeds"

        coEvery { breedRepository.getAllBreeds() } returns NetworkResult.Error(errorMessage)

        initViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(
                BreedFeedViewModel.UiState(breeds = emptyMap(), error = errorMessage)
            )
        }
    }
}
