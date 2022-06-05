package com.realityexpander.dictionary.feature_dictionary.presentation

import com.realityexpander.dictionary.feature_dictionary.domain.model.WordInfo
import com.realityexpander.dictionary.feature_dictionary.domain.repository.ErrorCode

data class WordInfoState(
    val wordInfoItems: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val errorCode: ErrorCode? = null
)
