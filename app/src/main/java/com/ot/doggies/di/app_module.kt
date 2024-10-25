@file:OptIn(ExperimentalCoroutinesApi::class)

package com.ot.doggies.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ot.doggies.data.ApiService
import com.ot.doggies.data.NetworkResultCallAdapterFactory
import com.ot.doggies.repositories.BreedRepository
import com.ot.doggies.repositories.BreedRepositoryImpl
import com.ot.doggies.ui.viewmodel.BreedDetailViewModel
import com.ot.doggies.ui.viewmodel.BreedFeedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit

const val baseUrl = "https://dog.ceo/api/"

val appModule = module {
    single<DispatcherProvider> { ApplicationDispatcherProvider() }
    single<BreedRepository> { BreedRepositoryImpl(get(), get()) }
    single<ApiService> { provideRetrofitService(get(), get()) }
    single<OkHttpClient> { provideOkHttpClient() }
    single<Converter.Factory> { provideConverter() }
    viewModel { BreedFeedViewModel(get(), get()) }
    viewModel { BreedDetailViewModel(get(), get()) }

}

private fun provideRetrofitService(
    converter: Converter.Factory,
    okHttpClient: OkHttpClient
): ApiService {

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(converter)
        .addCallAdapterFactory(NetworkResultCallAdapterFactory())
        .build()
        .create(ApiService::class.java)
}

fun provideConverter(): Converter.Factory {
    val json = Json { ignoreUnknownKeys = true }
    val contentType = "application/json".toMediaType()
    return json.asConverterFactory(contentType)
}

fun provideOkHttpClient(): OkHttpClient {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()
}

val doggiesApp = appModule