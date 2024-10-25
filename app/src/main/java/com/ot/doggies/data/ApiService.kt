package com.ot.doggies.data

import com.ot.doggies.data.model.BreedListResponse
import com.ot.doggies.data.model.ImagesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("breeds/list/all")
    suspend fun getBreeds(): NetworkResult<BreedListResponse>

    @GET("breed/{breed}/images/random/{amount}")
    suspend fun getRandomImagesFor(
        @Path("breed") breed: String,
        @Path("amount") amount: String
    ): NetworkResult<BreedListResponse>

    @GET("breed/{breed}/{subBreed}/images/random/{amount}")
    suspend fun getRandomImagesForSubBreed(
        @Path("breed") breed: String,
        @Path("subBreed") subBreed: String,
        @Path("amount") amount: Int
    ): NetworkResult<ImagesResponse>

    @GET("breed/{breed}/images/random/{amount}")
    suspend fun getRandomImagesForBreed(
        @Path("breed") breed: String,
        @Path("amount") amount: Int
    ): NetworkResult<ImagesResponse>
}