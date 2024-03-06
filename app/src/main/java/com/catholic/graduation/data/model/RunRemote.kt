package com.catholic.graduation.data.model

import android.util.Log
import com.catholic.graduation.presentation.util.Constants.TAG
import com.google.gson.Gson
import retrofit2.Response
import java.lang.RuntimeException

suspend fun <T> runRemote(block: suspend () -> Response<T>): Result<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: run {
                Result.failure(NullPointerException("Response body is null"))
            }
        } else {
            val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
            Result.failure(RuntimeException("Response error: $error"))
        }
    } catch (e: Exception) {
        Log.d(TAG, e.message.toString())
        Result.failure(e)
    }
}