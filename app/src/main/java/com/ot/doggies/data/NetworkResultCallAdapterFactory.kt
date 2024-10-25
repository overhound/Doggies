package com.ot.doggies.data

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResultCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResult<<Foo>> or Call<NetworkResult<out Foo>>"
        }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != NetworkResult::class.java) {
            return null
        }

        check(responseType is ParameterizedType) { "Response must be parameterized as NetworkResult<Foo> or NetworkResult<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)

        return NetworkResultAdapter<Any>(successBodyType)
    }
}