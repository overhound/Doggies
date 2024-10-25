package com.ot.doggies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ot.doggies.data.NetworkResult
import com.ot.doggies.di.DispatcherProvider
import com.ot.doggies.repositories.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BreedDetailViewModel(
    private val breedRepository: BreedRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<BreedDetailUiState>(
            BreedDetailUiState(
                images = emptyList(),
                isLoading = true
            )
        )
    val uiState = _uiState.asStateFlow()

     fun getImagesForBreed(breed: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcher.default()) {
            when (val images = breedRepository.getRandomImagesForBreed(breed)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(images = images.data, isLoading = false) }
                }

                is NetworkResult.Error -> {
                    _uiState.update { it.copy(error = images.message, isLoading = false) }
                }
            }
        }
    }

     fun getImagesForSubBreed(breed: String, subBreed: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcher.default()) {
            when (val images = breedRepository.getRandomImagesForSubBreed(breed, subBreed)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(images = images.data, isLoading = false) }
                }

                is NetworkResult.Error -> {
                    _uiState.update { it.copy(error = images.message, isLoading = false) }
                }
            }
        }
    }

    data class BreedDetailUiState(
        val images: List<String>,
        val isLoading: Boolean,
        val error: String = ""
    )
}