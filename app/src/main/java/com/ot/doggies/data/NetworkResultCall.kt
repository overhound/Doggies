package com.ot.doggies.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkResultCall<T : Any>(
    private val delegate: Call<T>
) : Call<NetworkResult<T>> {

    override fun enqueue(callback: Callback<NetworkResult<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val result = if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) NetworkResult.Success(body) else NetworkResult.Error("Something bad happened.")
                } else {
                    val errorJsonString = response.errorBody()?.string() ?: "Unknown error."
                    val errorMessage = try {
                        Json.parseToJsonElement(errorJsonString).jsonObject["message"]?.jsonPrimitive?.content
                    } catch (e: Exception) {
                        null
                    }
                    NetworkResult.Error(errorMessage ?: errorJsonString)
                }
                callback.onResponse(this@NetworkResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                callback.onResponse(this@NetworkResultCall, Response.success(NetworkResult.Error(throwable.localizedMessage?: "Something bad happened.")))
            }
        })
    }

    override fun clone(): Call<NetworkResult<T>> = NetworkResultCall(delegate.clone())

    override fun execute(): Response<NetworkResult<T>> {
        throw UnsupportedOperationException("NetworkResultCall does not support synchronous execution")
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}