package uk.ac.tees.mad.tuneflow.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tuneflow.model.dataclass.ApiPlaylistResponse
import uk.ac.tees.mad.tuneflow.model.dataclass.AuthResult
import uk.ac.tees.mad.tuneflow.model.dataclass.ErrorState
import uk.ac.tees.mad.tuneflow.model.dataclass.UiState
import uk.ac.tees.mad.tuneflow.model.dataclass.UserData
import uk.ac.tees.mad.tuneflow.model.dataclass.UserDetails
import uk.ac.tees.mad.tuneflow.model.repository.AuthRepository
import uk.ac.tees.mad.tuneflow.model.repository.DeezerRepository
import uk.ac.tees.mad.tuneflow.model.repository.NetworkRepository
import java.io.IOException
import java.net.SocketTimeoutException

class HomeScreenViewModel(
    private val networkRepository: NetworkRepository,
    private val authRepository: AuthRepository,
    private val deezerRepository: DeezerRepository
) : ViewModel() {

    private val _userDetails = MutableStateFlow<AuthResult<UserDetails>>(AuthResult.Loading)
    val userDetails: StateFlow<AuthResult<UserDetails>> = _userDetails.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _trendingSongs = MutableStateFlow<ApiPlaylistResponse?>(null)
    val trendingSongs: StateFlow<ApiPlaylistResponse?> = _trendingSongs.asStateFlow()

    private val _trendingAlbums = MutableStateFlow<ApiPlaylistResponse?>(null)
    val trendingAlbums: StateFlow<ApiPlaylistResponse?> = _trendingAlbums.asStateFlow()

    init {
        fetchUserDetails()
        getTrendingAlbums()
        getTrendingSongs()
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

    fun signOut() {
        authRepository.SignOut()
    }

    fun getTrendingSongs(){
        viewModelScope.launch{
            _uiState.value = UiState.Loading
            deezerRepository.topSongs().onSuccess {fetchedData ->
                _uiState.value = UiState.Success(fetchedData)
                _trendingSongs.value= fetchedData
            }.onFailure {exception ->
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
                _uiState.value = UiState.Error(errorState,exception.message.toString())
            }
        }
    }

    fun getTrendingAlbums(){
        viewModelScope.launch{
            _uiState.value = UiState.Loading
            deezerRepository.topWorldwide().onSuccess {fetchedData ->
                //_uiState.value = UiState.Success(fetchedData)
                _trendingAlbums.value= fetchedData
            }.onFailure {exception ->
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
                _uiState.value = UiState.Error(errorState,exception.message.toString())
            }

        }
    }
}