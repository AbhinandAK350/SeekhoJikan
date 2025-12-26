package com.abhinand.seekhojikan.core.network

sealed interface NetworkResource<out T> {

    data object Loading : NetworkResource<Nothing>
    data class Success<T>(val data: T?) : NetworkResource<T>

    data class Error(val message: String?, val code: Int? = null) : NetworkResource<Nothing>

}