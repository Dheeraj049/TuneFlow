package uk.ac.tees.mad.tuneflow.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tuneflow.model.dataclass.AuthResult
import uk.ac.tees.mad.tuneflow.model.dataclass.ErrorState
import uk.ac.tees.mad.tuneflow.model.dataclass.Track
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateNowPlaying
import uk.ac.tees.mad.tuneflow.model.dataclass.UserData
import uk.ac.tees.mad.tuneflow.model.dataclass.UserDetails
import uk.ac.tees.mad.tuneflow.model.repository.AuthRepository
import uk.ac.tees.mad.tuneflow.model.repository.DeezerRepository
import uk.ac.tees.mad.tuneflow.model.repository.FavoritePlaylistRepository
import uk.ac.tees.mad.tuneflow.model.repository.NetworkRepository
import java.io.IOException
import java.net.SocketTimeoutException

class NowPlayingScreenViewModel(
    private val networkRepository: NetworkRepository,
    private val deezerRepository: DeezerRepository,
    private val authRepository: AuthRepository,
    private val favoritePlaylistRepository: FavoritePlaylistRepository
) : ViewModel() {
    private val _uiStateNowPlaying = MutableStateFlow<UiStateNowPlaying>(UiStateNowPlaying.Loading)
    val uiStateNowPlaying: StateFlow<UiStateNowPlaying> = _uiStateNowPlaying.asStateFlow()

    private val _track = MutableStateFlow<Track?>(null)
    val track: StateFlow<Track?> = _track.asStateFlow()

    private val _favoriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favoriteTracks: StateFlow<List<Track>> = _favoriteTracks.asStateFlow()

    private val _trackIndex = MutableStateFlow(-1)
    val trackIndex: StateFlow<Int> = _trackIndex.asStateFlow()

    private val _userDetails = MutableStateFlow<AuthResult<UserDetails>>(AuthResult.Loading)
    val userDetails: StateFlow<AuthResult<UserDetails>> = _userDetails.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    init {
        fetchUserDetails()
        fetchDataFromDB()
    }

    private fun fetchUserDetails() {
        viewModelScope.launch {
            authRepository.getCurrentUserDetails().collect { result ->
                _userDetails.value = result
                if (result is AuthResult.Success) {
                    _userData.update {
                        it.copy(
                            userDetails = result.data, userId = authRepository.getCurrentUserId()
                        )
                    }
                }
            }
        }
    }

    fun fetchDataFromDB() {
        viewModelScope.launch {
            val favoriteTracks =
                favoritePlaylistRepository.getAllFavorites(_userData.value.userId.toString())
            _favoriteTracks.value = favoriteTracks
        }
    }

    fun updateTrackIndex(index: Int) {
        _trackIndex.value = index
    }

    fun checkIsFavorite(trackId: String): Boolean {
        return favoriteTracks.value.any { it.id.toString() == trackId }
    }

    fun addFavoriteTrack(id: String) {
        viewModelScope.launch {
            deezerRepository.getTrack(id).onSuccess { fetchedData ->
                favoritePlaylistRepository.insertFavorite(
                    fetchedData,
                    _userData.value.userId.toString()
                )
                val favoriteTracks =
                    favoritePlaylistRepository.getAllFavorites(_userData.value.userId.toString())
                _favoriteTracks.value = favoriteTracks
                fetchDataFromDB()
            }
        }
    }

    fun removeFavoriteTrack(id: String) {
        viewModelScope.launch {
            favoritePlaylistRepository.deleteFavorite(
                id.toLong(),
                _userData.value.userId.toString()
            )
            val favoriteTracks =
                favoritePlaylistRepository.getAllFavorites(_userData.value.userId.toString())
            _favoriteTracks.value = favoriteTracks
            fetchDataFromDB()
        }
    }

    fun onNext() {
        viewModelScope.launch {
            _uiStateNowPlaying.value = UiStateNowPlaying.Loading
            _trackIndex.value++
            getTrack(_favoriteTracks.value[_trackIndex.value].id.toString())
        }
    }

    fun onPrev() {
        viewModelScope.launch {
            _uiStateNowPlaying.value = UiStateNowPlaying.Loading
            _trackIndex.value--
            getTrack(_favoriteTracks.value[_trackIndex.value].id.toString())
        }
    }

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
                _uiStateNowPlaying.value =
                    UiStateNowPlaying.Error(errorState, exception.message.toString())
            }
        }
    }
}