package com.ot.doggies.repositories

import com.ot.doggies.data.ApiService
import com.ot.doggies.data.NetworkResult
import com.ot.doggies.data.mapSuccess
import com.ot.doggies.di.DispatcherProvider
import kotlinx.coroutines.withContext


interface BreedRepository {
    suspend fun getAllBreeds(): NetworkResult<Map<String, List<String>>>

    suspend fun getRandomImagesForBreed(
        breed: String,
        amount: Int = 10
    ): NetworkResult<List<String>>

    suspend fun getRandomImagesForSubBreed(
        breed: String,
        subBreed: String,
        amount: Int = 10
    ): NetworkResult<List<String>>
}

class BreedRepositoryImpl(
    private val apiService: ApiService,
    private val dispatchers: DispatcherProvider,
) : BreedRepository {

    override suspend fun getAllBreeds(): NetworkResult<Map<String, List<String>>> =
        withContext(dispatchers.io()) {
            apiService.getBreeds().mapSuccess { it.message.capitalizeBreeds() }
        }

    override suspend fun getRandomImagesForBreed(
        breed: String,
        amount: Int
    ): NetworkResult<List<String>> =
        withContext(dispatchers.io()) {
            apiService.getRandomImagesForBreed(breed.lowercase(), amount).mapSuccess { it.message }
        }


    override suspend fun getRandomImagesForSubBreed(
        breed: String,
        subBreed: String,
        amount: Int
    ): NetworkResult<List<String>> =
        withContext(dispatchers.io()) {
            apiService.getRandomImagesForSubBreed(breed, subBreed, amount)
                .mapSuccess { it.message }
        }

    private fun Map<String, List<String>>.capitalizeBreeds(): Map<String, List<String>> {
        return map { (breed, subBreeds) ->
            breed.replaceFirstChar { it.uppercaseChar() } to subBreeds.map { it.replaceFirstChar { it.uppercaseChar() } }
        }.toMap()
    }
}