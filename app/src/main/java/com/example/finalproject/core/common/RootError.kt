package com.example.finalproject.core.common

sealed interface RootError {
    val message: String

    data class Network(override val message: String) : RootError
    data class Http(val code: Int, override val message: String) : RootError
    data class Serialization(override val message: String) : RootError
    data class Cache(override val message: String) : RootError
    data class Unexpected(override val message: String) : RootError
}
