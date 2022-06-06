package com.realityexpander.dictionary.feature_dictionary.domain.use_case

import com.realityexpander.dictionary.core.util.Resource
import com.realityexpander.dictionary.core.util.lettersOnly
import com.realityexpander.dictionary.feature_dictionary.domain.model.WordInfo
import com.realityexpander.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWordInfo(
    private val repository: WordInfoRepository
) {

    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> {
        if(word.isBlank() || word.trim().lettersOnly().isEmpty()) {
            return flow {  }
        }
        return repository.getWordInfo(word)
    }
}