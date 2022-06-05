package com.realityexpander.dictionary.feature_dictionary.data.repository

import com.realityexpander.dictionary.core.util.Resource
import com.realityexpander.dictionary.feature_dictionary.data.local.WordInfoDao
import com.realityexpander.dictionary.feature_dictionary.data.remote.DictionaryApi
import com.realityexpander.dictionary.feature_dictionary.domain.model.WordInfo
import com.realityexpander.dictionary.feature_dictionary.domain.repository.ErrorCode
import com.realityexpander.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao
) : WordInfoRepository {

    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading(data = emptyList<WordInfo>()))

        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(data = wordInfos))

        try {
            val remoteWordInfos = api.getWordInfo(word)
            // println(remoteWordInfos) // leave for debug

            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })
        } catch (e: HttpException) {
            if (e.code() != ErrorCode.WORD_NOT_FOUND.code) {
                emit(
                    Resource.Error(
                        message = "Oops, something went wrong!",
                        data = wordInfos,
                        errorCode = ErrorCode.UNKNOWN
                    )
                )
            }
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = wordInfos
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
                    data = wordInfos
                )
            )
        } finally {
            val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }

            // Word was not found from API *AND* it was not found from DB
            if(newWordInfos.isEmpty()) {
                emit(Resource.Error(
                    message = "Definition not found.",
                    data = emptyList<WordInfo>(),
                    errorCode = ErrorCode.WORD_NOT_FOUND)
                )
            } else {
                emit(Resource.Success(data = newWordInfos))
            }
        }

    }
}