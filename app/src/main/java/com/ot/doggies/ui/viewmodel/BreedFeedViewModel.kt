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

class BreedFeedViewModel(
    private val breedRepository: BreedRepository,
    dispatcher: DispatcherProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState(emptyMap()))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher.default()) {
            when (val breeds = breedRepository.getAllBreeds()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(breeds = breeds.data) }
                }

                is NetworkResult.Error -> {
                    _uiState.update { it.copy(error = breeds.message) }
                }
            }
        }
    }

    data class UiState(
        val breeds: Map<String, List<String>>,
        val error: String = ""
    )
}