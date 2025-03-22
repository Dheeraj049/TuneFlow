package uk.ac.tees.mad.tuneflow.model.dataclass

sealed class UiStateTrendingAlbums {
    object Loading : UiStateTrendingAlbums()
    data class Success(val response: ApiPlaylistResponse) : UiStateTrendingAlbums()
    data class Error(val errorState: ErrorState, val message: String) : UiStateTrendingAlbums()
}

sealed class UiStateTrendingSongs {
    object Loading : UiStateTrendingSongs()
    data class Success(val response: ApiPlaylistResponse) : UiStateTrendingSongs()
    data class Error(val errorState: ErrorState, val message: String) : UiStateTrendingSongs()
}

sealed class UiStateSearch {
    object Loading : UiStateSearch()
    data class Success(val response: ApiSearchResponse) : UiStateSearch()
    data class Error(val errorState: ErrorState, val message: String) : UiStateSearch()
}

sealed class UiStateNowPlaying {
    object Loading : UiStateNowPlaying()
    data class Success(val response: Track) : UiStateNowPlaying()
    data class Error(val errorState: ErrorState, val message: String) : UiStateNowPlaying()
}