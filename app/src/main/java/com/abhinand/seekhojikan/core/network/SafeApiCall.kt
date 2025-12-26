package com.abhinand.seekhojikan.core.network

import retrofit2.HttpException
import java.net.UnknownHostException

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): NetworkResource<T> {
    return try {
        NetworkResource.Success(apiCall())
    } catch (e: HttpException) {
        NetworkResource.Error(
            message = e.response()?.errorBody()?.string() ?: e.message(),
            code = e.code()
        )
    } catch (e: UnknownHostException) {
        NetworkResource.Error("No internet connection")
    } catch (e: Exception) {
        NetworkResource.Error(e.localizedMessage ?: "Unknown error")
    }
}