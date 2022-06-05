package com.realityexpander.dictionary.feature_dictionary.domain.repository

import com.realityexpander.dictionary.core.util.Resource
import com.realityexpander.dictionary.feature_dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

enum class ErrorCode(val code: Int) {
    WORD_NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    UNKNOWN(0)
}

interface WordInfoRepository {

    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>
}