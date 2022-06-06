package com.realityexpander.dictionary.feature_dictionary.presentation

import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.dictionary.core.util.Resource
import com.realityexpander.dictionary.core.util.lettersOnly
import com.realityexpander.dictionary.feature_dictionary.domain.repository.ErrorCode
import com.realityexpander.dictionary.feature_dictionary.domain.use_case.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf(WordInfoState())
    val state: State<WordInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    val mediaPlayer = MediaPlayer()

    fun onSearch(query: String) {
        _searchQuery.value = query
        val cleanQuery = query.trim().lettersOnly()
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500L) // debounce input
            if (cleanQuery.isEmpty()) {
                _state.value = state.value.copy(
                    isLoading = false,
                    isError = false,
                )
                return@launch
            }

            // Set loading state
            _state.value = state.value.copy(
                isLoading = true,
                isError = false,
                errorMessage = null,
                errorCode = null,
            )

            getWordInfo(cleanQuery)
                .onEach { result -> // flow collected here
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false,
                                isError = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false,
                                isError = true,
                                errorMessage = result.message,
                                errorCode = result.errorCode
                            )

                            // Show snackbar if error is not WORD_NOT_FOUND
                            if (result.errorCode != ErrorCode.WORD_NOT_FOUND) {
                                _eventFlow.emit(
                                    UIEvent.ShowSnackbar(
                                        result.message ?: "Unknown error"
                                    )
                                )
                            }
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

    fun playAudio(url: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(URL(url).toString())
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                    UIEvent.ShowSnackbar("Error playing audio").let {
                        _eventFlow.emit(it)
                    }
                }
            }
        }
    }
}
