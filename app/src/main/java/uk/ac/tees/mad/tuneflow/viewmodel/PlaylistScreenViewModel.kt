package uk.ac.tees.mad.tuneflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tuneflow.model.dataclass.AuthResult
import uk.ac.tees.mad.tuneflow.model.dataclass.Track
import uk.ac.tees.mad.tuneflow.model.dataclass.UserData
import uk.ac.tees.mad.tuneflow.model.dataclass.UserDetails
import uk.ac.tees.mad.tuneflow.model.repository.AuthRepository
import uk.ac.tees.mad.tuneflow.model.repository.DeezerRepository
import uk.ac.tees.mad.tuneflow.model.repository.FavoritePlaylistRepository
import uk.ac.tees.mad.tuneflow.model.repository.NetworkRepository

class PlaylistScreenViewModel(
    private val networkRepository: NetworkRepository,
    private val authRepository: AuthRepository,
    private val deezerRepository: DeezerRepository,
    private val favoritePlaylistRepository: FavoritePlaylistRepository
) : ViewModel() {

    private val _favoriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favoriteTracks: StateFlow<List<Track>> = _favoriteTracks.asStateFlow()

    private val _userDetails = MutableStateFlow<AuthResult<UserDetails>>(AuthResult.Loading)
    val userDetails: StateFlow<AuthResult<UserDetails>> = _userDetails.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    init {
        fetchUserDetails()
        fetchFavoritePlaylist()
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

    fun fetchFavoritePlaylist() {
        viewModelScope.launch {
            val favoritePlaylist =
                favoritePlaylistRepository.getAllFavorites(_userData.value.userId.toString())
            _favoriteTracks.value = favoritePlaylist
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
        }
    }

}