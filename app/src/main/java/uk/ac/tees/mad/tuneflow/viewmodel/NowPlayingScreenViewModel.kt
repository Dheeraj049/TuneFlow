package uk.ac.tees.mad.tuneflow.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tuneflow.model.dataclass.ErrorState
import uk.ac.tees.mad.tuneflow.model.dataclass.Track
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateNowPlaying
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateTrendingAlbums
import uk.ac.tees.mad.tuneflow.model.repository.AuthRepository
import uk.ac.tees.mad.tuneflow.model.repository.DeezerRepository
import uk.ac.tees.mad.tuneflow.model.repository.NetworkRepository
import java.io.IOException
import java.net.SocketTimeoutException

class NowPlayingScreenViewModel(
    private val networkRepository: NetworkRepository,
    private val deezerRepository: DeezerRepository
): ViewModel() {
    private val _uiStateNowPlaying = MutableStateFlow<UiStateNowPlaying>(UiStateNowPlaying.Loading)
    val uiStateNowPlaying: StateFlow<UiStateNowPlaying> = _uiStateNowPlaying.asStateFlow()

    private val _track = MutableStateFlow<Track?>(null)
    val track: StateFlow<Track?> = _track.asStateFlow()


    fun getTrack(id: String) {
        viewModelScope.launch {
            _uiStateNowPlaying.value = UiStateNowPlaying.Loading
            deezerRepository.getTrack(id).onSuccess { fetchedData ->
                _uiStateNowPlaying.value = UiStateNowPlaying.Success(fetchedData)
                _track.value = fetchedData!!
            }.onFailure { exception ->
                val errorState = when (exception) {
                    is SocketTimeoutException -> {
                        // `Log.e` is used to log errors to the console.
                        Log.e("myApp", "Connection timed out: ${exception.message}")
                        ErrorState.TimeoutError
                    }

                    is IOException -> {
                        Log.e("myApp", "No internet connection: ${exception.message}")
                        ErrorState.NetworkError
                    }

                    else -> {
                        Log.e("myApp", "Error fetching items: ${exception.message}")
                        ErrorState.UnknownError
                    }
                }
                _uiStateNowPlaying.value = UiStateNowPlaying.Error(errorState, exception.message.toString())
            }
        }
    }
}