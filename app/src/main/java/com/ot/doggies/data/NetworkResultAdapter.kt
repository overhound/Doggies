package com.ot.doggies.data

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResultAdapter<T : Any>(private val responseType: Type) : CallAdapter<T, Call<NetworkResult<T>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<T>): Call<NetworkResult<T>> {
        return NetworkResultCall(call)
    }
}