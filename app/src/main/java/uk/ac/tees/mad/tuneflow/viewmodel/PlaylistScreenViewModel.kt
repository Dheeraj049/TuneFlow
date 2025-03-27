package uk.ac.tees.mad.tuneflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tuneflow.model.dataclass.Track
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

    init {
        fetchFavoritePlaylist()
    }

    fun fetchFavoritePlaylist() {
        viewModelScope.launch {
            val favoritePlaylist = favoritePlaylistRepository.getAllFavorites()
            _favoriteTracks.value = favoritePlaylist
        }
    }

    fun removeFavoriteTrack(id: String) {
        viewModelScope.launch {
            favoritePlaylistRepository.deleteFavorite(id.toLong())
            val favoriteTracks = favoritePlaylistRepository.getAllFavorites()
            _favoriteTracks.value = favoriteTracks
        }
    }

}