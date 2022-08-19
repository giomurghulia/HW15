package com.example.hw15

sealed class Resource<T:Any > {
    data class Success<T :Any>(val items: T) : Resource<T>()
    data class Error<T :Any>(val errorMessage: String) : Resource<T>()

}

