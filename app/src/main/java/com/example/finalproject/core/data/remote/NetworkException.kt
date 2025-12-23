package com.example.finalproject.core.data.remote

sealed class NetworkException(message: String) : Exception(message) {
    class HttpError(val code: Int, message: String) : NetworkException(message)
    class Network(message: String) : NetworkException(message)
    class Unexpected(message: String) : NetworkException(message)
}
