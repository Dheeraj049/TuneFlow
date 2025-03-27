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
import uk.ac.tees.mad.tuneflow.model.dataclass.ApiSearchResponse
import uk.ac.tees.mad.tuneflow.model.dataclass.AuthResult
import uk.ac.tees.mad.tuneflow.model.dataclass.ErrorState
import uk.ac.tees.mad.tuneflow.model.dataclass.Track
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateSearch
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateTrendingAlbums
import uk.ac.tees.mad.tuneflow.model.dataclass.UiStateTrendingSongs
import uk.ac.tees.mad.tuneflow.model.dataclass.UserData
import uk.ac.tees.mad.tuneflow.model.dataclass.UserDetails
import uk.ac.tees.mad.tuneflow.model.repository.AuthRepository
import uk.ac.tees.mad.tuneflow.model.repository.DeezerRepository
import uk.ac.tees.mad.tuneflow.model.repository.FavoritePlaylistRepository
import uk.ac.tees.mad.tuneflow.model.repository.NetworkRepository
import java.io.IOException
import java.net.SocketTimeoutException

class HomeScreenViewModel(
    private val networkRepository: NetworkRepository,
    private val authRepository: AuthRepository,
    private val deezerRepository: DeezerRepository,
    private val favoritePlaylistRepository: FavoritePlaylistRepository
) : ViewModel() {

    private val _userDetails = MutableStateFlow<AuthResult<UserDetails>>(AuthResult.Loading)
    val userDetails: StateFlow<AuthResult<UserDetails>> = _userDetails.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    private val _uiStateTrendingAlbums = MutableStateFlow<UiStateTrendingAlbums>(UiStateTrendingAlbums.Loading)
    val uiStateTrendingAlbums: StateFlow<UiStateTrendingAlbums> = _uiStateTrendingAlbums.asStateFlow()

    private val _uiStateTrendingSongs = MutableStateFlow<UiStateTrendingSongs>(UiStateTrendingSongs.Loading)
    val uiStateTrendingSongs: StateFlow<UiStateTrendingSongs> = _uiStateTrendingSongs.asStateFlow()

    private val _uiStateSearch = MutableStateFlow<UiStateSearch>(UiStateSearch.Loading)
    val uiStateSearch: StateFlow<UiStateSearch> = _uiStateSearch.asStateFlow()

    private val _trendingSongs = MutableStateFlow<ApiPlaylistResponse?>(null)
    val trendingSongs: StateFlow<ApiPlaylistResponse?> = _trendingSongs.asStateFlow()

    private val _trendingAlbums = MutableStateFlow<ApiPlaylistResponse?>(null)
    val trendingAlbums: StateFlow<ApiPlaylistResponse?> = _trendingAlbums.asStateFlow()

    private val _searchText= MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _searchResult= MutableStateFlow<ApiSearchResponse?>(null)
    val searchResult: StateFlow<ApiSearchResponse?> = _searchResult.asStateFlow()

    private val _clickedTrackId= MutableStateFlow("")
    val clickedTrackId: StateFlow<String> = _clickedTrackId.asStateFlow()

    private val _trackName= MutableStateFlow("")
    val trackName: StateFlow<String> = _trackName.asStateFlow()


    private val _favoriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favoriteTracks: StateFlow<List<Track>> = _favoriteTracks.asStateFlow()

    init {
        fetchUserDetails()
        getTrendingAlbums()
        getTrendingSongs()
    }

    fun updateSearchResultToEmpty(){
        _searchResult.value = null
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
            val favoriteTracks = favoritePlaylistRepository.getAllFavorites()
            _favoriteTracks.value = favoriteTracks
        }
    }

    fun fetchDataFromDB(){
        viewModelScope.launch {
            val favoriteTracks = favoritePlaylistRepository.getAllFavorites()
            _favoriteTracks.value = favoriteTracks
            if(favoriteTracks.isNotEmpty())
            {updateClickedTrackIdAndName(favoriteTracks[0].id.toString(), favoriteTracks[0].title)}
        }
    }

    fun signOut() {
        authRepository.SignOut()
    }

    fun updateSearchText(newText: String) {
        _searchText.value = newText
    }

    fun updateClickedTrackIdAndName(newId: String, newName: String) {
        _clickedTrackId.value = newId
        _trackName.value = newName
    }

    fun checkIsFavorite(trackId: String): Boolean {
        return favoriteTracks.value.any { it.id.toString() == trackId }
    }

    fun addFavoriteTrack(id: String) {
        viewModelScope.launch {
            deezerRepository.getTrack(id).onSuccess { fetchedData ->
                favoritePlaylistRepository.insertFavorite(fetchedData)
                val favoriteTracks = favoritePlaylistRepository.getAllFavorites()
                _favoriteTracks.value = favoriteTracks
                fetchDataFromDB()
            }
        }
    }

    fun removeFavoriteTrack(id: String) {
        viewModelScope.launch {
            favoritePlaylistRepository.deleteFavorite(id.toLong())
            val favoriteTracks = favoritePlaylistRepository.getAllFavorites()
            _favoriteTracks.value = favoriteTracks
            fetchDataFromDB()
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _uiStateSearch.value = UiStateSearch.Loading
            deezerRepository.search(query).onSuccess { fetchedData ->
                _uiStateSearch.value = UiStateSearch.Success(fetchedData)
                _searchResult.value = fetchedData
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
                _uiStateSearch.value = UiStateSearch.Error(errorState, exception.message.toString())
            }
        }
    }

    fun getTrendingSongs(){
        viewModelScope.launch{
            _uiStateTrendingSongs.value = UiStateTrendingSongs.Loading
            deezerRepository.topSongs().onSuccess {fetchedData ->
                _uiStateTrendingSongs.value = UiStateTrendingSongs.Success(fetchedData)
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
                _uiStateTrendingSongs.value = UiStateTrendingSongs.Error(errorState,exception.message.toString())
            }
        }
    }

    fun getTrendingAlbums(){
        viewModelScope.launch{
            _uiStateTrendingAlbums.value = UiStateTrendingAlbums.Loading
            deezerRepository.topWorldwide().onSuccess {fetchedData ->
                _uiStateTrendingAlbums.value = UiStateTrendingAlbums.Success(fetchedData)
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
                _uiStateTrendingAlbums.value = UiStateTrendingAlbums.Error(errorState,exception.message.toString())
            }

        }
    }
}