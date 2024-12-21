package com.stein.mahoyinkuima.common

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data object Begin : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure(val message: String) : Resource<Nothing>()
}
