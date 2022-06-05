package com.realityexpander.dictionary.core.util

import com.realityexpander.dictionary.feature_dictionary.domain.repository.ErrorCode

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null, val errorCode: ErrorCode? = null): Resource<T>(data, message)
}
