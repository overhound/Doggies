package com.ot.doggies.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ot.doggies.ui.viewmodel.BreedDetailViewModel
import com.ot.doggies.ui.viewmodel.BreedDetailViewModel.BreedDetailUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun BreedDetailScreen(
    breed: String,
    subBreed: String,
    viewModel: BreedDetailViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        if (subBreed.isNotBlank()) {
            viewModel.getImagesForSubBreed(breed.lowercase(), subBreed.lowercase())
        } else {
            viewModel.getImagesForBreed(breed = breed)
        }
    }
    BreedDetailContent(uiState.value)
}

@Composable
fun BreedDetailContent(uiState: BreedDetailUiState) {
    Box(modifier = Modifier.fillMaxSize().padding()){


    if (uiState.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(uiState.images) { imageUrl ->
                BreedImageItem(imageUrl = imageUrl)
            }
        }
    }
    }
}

@Composable
fun BreedImageItem(imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Breed Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}