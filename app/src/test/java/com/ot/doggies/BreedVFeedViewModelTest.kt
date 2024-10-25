package com.ot.doggies

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ot.doggies.data.NetworkResult
import com.ot.doggies.repositories.BreedRepository
import com.ot.doggies.ui.viewmodel.BreedDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BreedDetailViewModelTest {
    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    private val breedRepository = mockk<BreedRepository>()
    private val dispatcherProvider = coroutinesTestRule.testDispatcherProvider

    private lateinit var viewModel: BreedDetailViewModel

    private fun initViewModel() {
        viewModel = BreedDetailViewModel(breedRepository, dispatcherProvider)
    }

    @Test
    fun `getImagesForBreed should update uiState with images on success`() = runTest {
        val breed = "hound"
        val images = listOf("image1.jpg", "image2.jpg")

        coEvery { breedRepository.getRandomImagesForBreed(breed) } returns NetworkResult.Success(
            images
        )
        initViewModel()
        viewModel.getImagesForBreed(breed)

        viewModel.uiState.test {
            assertThat(
                BreedDetailViewModel.BreedDetailUiState(
                    isLoading = false,
                    images = images
                )
            ).isEqualTo(awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getImagesForBreed should update uiState with error on failure`() = runTest {
        val breed = "hound"
        val errorMessage = "Failed to fetch images"

        coEvery { breedRepository.getRandomImagesForBreed(breed) } returns NetworkResult.Error(
            errorMessage
        )
        initViewModel()
        viewModel.getImagesForBreed(breed)

        viewModel.uiState.test {
            assertThat(
                BreedDetailViewModel.BreedDetailUiState(
                    isLoading = false,
                    images = emptyList(),
                    error = errorMessage
                )
            ).isEqualTo(awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getImagesForSubBreed should update uiState with images on success`() = runTest {
        val breed = "hound"
        val subBreed = "afghan"
        val images = listOf("image1.jpg", "image2.jpg")

        coEvery {
            breedRepository.getRandomImagesForSubBreed(
                breed,
                subBreed
            )
        } returns NetworkResult.Success(images)
        initViewModel()
        viewModel.getImagesForSubBreed(breed, subBreed)
        viewModel.uiState.test {
            assertThat(
                BreedDetailViewModel.BreedDetailUiState(
                    isLoading = false,
                    images = images
                )
            ).isEqualTo(awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getImagesForSubBreed should update uiState with error on failure`() = runTest {
        val breed = "hound"
        val subBreed = "afghan"
        val errorMessage = "Failed to fetch images"

        coEvery {
            breedRepository.getRandomImagesForSubBreed(
                breed,
                subBreed
            )
        } returns NetworkResult.Error(errorMessage)
        initViewModel()
        viewModel.getImagesForSubBreed(breed, subBreed)

        viewModel.uiState.test {
            assertThat(
                BreedDetailViewModel.BreedDetailUiState(
                    isLoading = false,
                    images = emptyList(),
                    error = errorMessage
                )
            ).isEqualTo(
                awaitItem()
            )
            cancelAndConsumeRemainingEvents()
        }
    }
}
