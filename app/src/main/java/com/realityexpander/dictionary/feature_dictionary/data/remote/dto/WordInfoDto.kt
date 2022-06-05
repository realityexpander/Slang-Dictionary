package com.realityexpander.dictionary.feature_dictionary.data.remote.dto

import com.realityexpander.dictionary.feature_dictionary.data.local.entity.WordInfoEntity


data class WordInfoDto(
    val meanings: List<MeaningDto>,
    val origin: String,
    val phonetic: String,
    val phonetics: List<PhoneticDto>,
    val word: String
) {
    fun toWordInfoEntity(): WordInfoEntity {
        return WordInfoEntity(
            meanings = meanings.map { it.toMeaning() },
            origin = origin ?: "Unknown",
            phonetic = phonetic ?: "//",
            word = word
        )
    }
}